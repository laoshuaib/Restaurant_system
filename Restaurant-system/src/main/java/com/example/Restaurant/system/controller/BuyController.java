package com.example.Restaurant.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Restaurant.system.common.Result;
import com.example.Restaurant.system.entity.Buy;
import com.example.Restaurant.system.service.BuyServiceImpl;
import com.example.Restaurant.system.untils.CheckUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/buy")
public class BuyController {
    @Resource
    BuyServiceImpl buyService;

    @GetMapping("/findBuyByIngredient")
    public Result<?> findBuy(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam String search) {
        List<Buy> buyList = buyService.listWithName();//默认值
        if (StrUtil.isNotBlank(search)) {
            if (CheckUtil.isNum(search)) {
                buyList = buyService.listByIngredientID(new BigInteger(search));
            } else {
                buyList = buyService.listByIngredientName(search);
            }
        }
        return getResPage(pageNum, pageSize, buyList);
    }

    @GetMapping("/findBuyByUser")
    public Result<?> findBuyByUser(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam String search) {
        List<Buy> buyList = buyService.listWithName();//默认值
        if (StrUtil.isNotBlank(search)) {
            if (CheckUtil.isNum(search)) {
                buyList = buyService.listByUserID(new BigInteger(search));
            } else {
                buyList = buyService.listByUserName(search);
            }
        }
        return getResPage(pageNum, pageSize, buyList);
    }

    @GetMapping("/viewBuy")
    public Result<?> findBuyPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Buy> buyList = buyService.listWithName();
        return getResPage(pageNum, pageSize, buyList);
    }

    @GetMapping("/unionFind")
    public Result<?> unionFind(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam String ingredientSearch, @RequestParam String userSearch) {
        List<Buy> resList = buyService.listWithName();//默认值
        List<Buy> ingredientList = new ArrayList<>();
        List<Buy> userList = new ArrayList<>();
        if (StrUtil.isNotBlank(ingredientSearch)) {
            if (CheckUtil.isNum(ingredientSearch)) {
                ingredientList = buyService.listByIngredientID(new BigInteger(ingredientSearch));
            } else {
                ingredientList = buyService.listByIngredientName(ingredientSearch);
            }
        }
        if (StrUtil.isNotBlank(userSearch)) {
            if (CheckUtil.isNum(userSearch)) {
                userList = buyService.listByUserID(new BigInteger(userSearch));
            } else {
                userList = buyService.listByUserName(userSearch);
            }
        }
        if(!ingredientList.isEmpty()||!userList.isEmpty())
            resList = (List<Buy>) CollectionUtils.intersection(ingredientList,userList);
        return getResPage(pageNum, pageSize, resList);
    }

    /**
     * 分页函数
     * @param currentPage 当前页数
     * @param pageSize    每一页的数据条数
     * @param list        要进行分页的数据列表
     * @return 当前页要展示的数据
     */
    @Nullable
    private Result<?> getResPage(Integer currentPage, Integer pageSize, @NotNull List<Buy> list) {
        Page<Buy> resPage = new Page<>();
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
        List<Buy> pageList = new ArrayList<>();
        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }
        resPage.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
        return Result.success(resPage);
    }
}
