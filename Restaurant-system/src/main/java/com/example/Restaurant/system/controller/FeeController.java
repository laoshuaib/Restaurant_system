package com.example.Restaurant.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Restaurant.system.common.Result;
import com.example.Restaurant.system.entity.Fee;
import com.example.Restaurant.system.service.FeeServiceImpl;
import com.example.Restaurant.system.service.UserServiceImpl;
import com.example.Restaurant.system.untils.CheckUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController//返回json数据的controller
@RequestMapping("/fee")//路由
public class FeeController {
    @Resource
    private FeeServiceImpl feeService;
    @Resource
    private UserServiceImpl userService;

    @PostMapping("/save")
    public Result<?> save(@NotNull @RequestBody Fee fee) {
        if (Objects.equals(fee.getType(), ""))
            return Result.error(-1, "费用类型未设置");
        if (fee.getPrice() == null)
            return Result.error(-1, "费用价格未设置");
        if (!userService.userIDCheck(fee.getUserID()))
            return Result.error(-1, "费用处理者错误");
        if (userService.userJobCheck(fee.getUserID()) != 1)
            return Result.error(-1, "费用处理者权限不足");
        feeService.save(fee);
        return Result.success();
    }

    @GetMapping("/view")
    public Result<?> findBuyPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Fee> feeList = feeService.listWithName();
        return getResPage(pageNum, pageSize, feeList);
    }

    @GetMapping("/findByFee")
    public Result<?> findByFee(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam String search) {
        List<Fee> feeList = feeService.listWithName();//默认值
        if (StrUtil.isNotBlank(search)) {
            if (CheckUtil.isNum(search)) {
                feeList = feeService.listByIDWithName(new BigInteger(search));
            } else {
                feeList = feeService.listByTypeWithName(search);
            }
        }
        return getResPage(pageNum, pageSize, feeList);
    }

    @GetMapping("/findByUser")
    public Result<?> findByUser(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam String search) {
        List<Fee> feeList = feeService.listWithName();//默认值
        if (StrUtil.isNotBlank(search)) {
            if (CheckUtil.isNum(search)) {
                feeList = feeService.listByUserIDWithName(new BigInteger(search));
            } else {
                feeList = feeService.listByUserNameWithName(search);
            }
        }
        return getResPage(pageNum, pageSize, feeList);
    }

    @GetMapping("/unionFind")
    public Result<?> unionFind(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam String feeSearch, @RequestParam String userSearch) {
        List<Fee> resList = feeService.listWithName();//默认值
        List<Fee> feeList = new ArrayList<>();
        List<Fee> userList = new ArrayList<>();
        if (StrUtil.isNotBlank(feeSearch)) {
            if (CheckUtil.isNum(feeSearch)) {
                feeList = feeService.listByIDWithName(new BigInteger(feeSearch));
            } else {
                feeList = feeService.listByTypeWithName(feeSearch);
            }
        }
        if (StrUtil.isNotBlank(userSearch)) {
            if (CheckUtil.isNum(userSearch)) {
                userList = feeService.listByUserIDWithName(new BigInteger(userSearch));
            } else {
                userList = feeService.listByUserNameWithName(userSearch);
            }
        }
        if (!feeList.isEmpty() || !userList.isEmpty())
            resList = (List<Fee>) CollectionUtils.intersection(feeList, userList);
        return getResPage(pageNum, pageSize, resList);
    }

    @PostMapping("/excelUpload")
    public Result<?> upload(@NotNull MultipartFile file, @RequestParam String userID) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        //表头校验
        List<List<Object>> readTest = reader.read(0, 0);
        List<Object> legalTitle = new ArrayList<>();
        legalTitle.add("费用类型");
        legalTitle.add("总价");
        legalTitle.add("注释");
        if (!readTest.contains(legalTitle))
            return Result.error(-1, "请检查excel表头设置是否正确");
        //准备批量注入
        BigInteger user = new BigInteger(userID);
        List<List<Object>> readAll = reader.read(1);
        for (List<Object> fee : readAll) {
            Fee input = new Fee();
            input.setType((String) fee.get(0));
            input.setUserID(user);
            input.setPrice(Double.parseDouble(fee.get(1).toString()));
            if (fee.size() > 2)
                input.setNote((String) fee.get(2));
            feeService.save(input);
        }
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<?> delete(@NotNull @RequestBody Fee fee){
        feeService.removeById(fee.getID());
        return Result.success();
    }


    /**
     * 分页函数
     * @param currentPage 当前页数
     * @param pageSize    每一页的数据条数
     * @param list        要进行分页的数据列表
     * @return 当前页要展示的数据
     */
    @Nullable
    private Result<?> getResPage(Integer currentPage, Integer pageSize, @NotNull List<Fee> list) {
        Page<Fee> resPage = new Page<>();
        int size = list.size();
        if (size == 0) {
            return null;
        }
        if (pageSize > size) {
            pageSize = size;
        }
        // 求出最大页数，防止currentPage越界
        int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
        if (currentPage > maxPage) {
            currentPage = maxPage;
        }
        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
        List<Fee> pageList = new ArrayList<>();
        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }
        resPage.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
        return Result.success(resPage);
    }
}
