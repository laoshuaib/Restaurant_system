package com.example.Restaurant.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Restaurant.system.common.Result;
import com.example.Restaurant.system.entity.Cuisine;
import com.example.Restaurant.system.entity.CuisineUsing;
import com.example.Restaurant.system.service.CuisineServiceImpl;

import com.example.Restaurant.system.service.CuisineUsingServiceImpl;
import com.example.Restaurant.system.untils.CheckUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController//返回json数据的controller
@RequestMapping("/cuisine")//路由

public class CuisineController {
    @Resource
    private CuisineServiceImpl cuisineService;

    @Resource
    private CuisineUsingServiceImpl cuisineUsingService;

    @SuppressWarnings("SuspiciousToArrayCall")//屏蔽警告
    @PostMapping("/save")
    public Result<?> save(@NotNull @RequestBody Map<String,Object> input){
        //存储cuisine
        Cuisine cuisine = JSONUtil.toBean(JSONUtil.toJsonStr(input.get("cuisine")), Cuisine.class);
        if(Objects.equals(cuisine.getName(), ""))
            return Result.error(-1,"菜名未填写");
        if(!cuisineService.nameCheck(cuisine))
            return Result.error(-1,"已存在该菜名");
        if(cuisine.getPrice()==null)
            return Result.error(-1,"价格必须填写");
        if(cuisine.getPrice()<0)
            return Result.error(-1,"价格不能小于0");
        cuisine.setStatus(0);//默认激活
        cuisineService.save(cuisine);
        //获取cuisineID
        LambdaQueryWrapper<Cuisine> cuisineWrapper = Wrappers.lambdaQuery();
        cuisineWrapper.eq(Cuisine::getStatus,0);//仅选择已激活菜品
        cuisineWrapper.eq(Cuisine::getName,cuisine.getName());
        BigInteger cuisineID = cuisineService.getOne(cuisineWrapper).getID();
        //获取cuisineUsing数组（批量处理ID）
        CuisineUsing[] cuisineUsings =
                JSONUtil.parseArray(JSONUtil.toJsonStr(input.get("cuisineUsing"))).toArray(new CuisineUsing[0]);
        for(CuisineUsing cuisineUsing:cuisineUsings){
            cuisineUsing.setCuisineID(cuisineID);
        }
        //准备存储
        List<CuisineUsing> data = Arrays.asList(cuisineUsings);
        cuisineUsingService.saveBatch(data);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<?> update(@NotNull @RequestBody Cuisine cuisine) {
        if (Objects.equals(cuisine.getName(), ""))
            return Result.error(-1, "菜品名未填写");
        if (!cuisineService.nameCheck(cuisine) &&
                !Objects.equals(cuisineService.getById(cuisine.getID()).getName(), cuisine.getName()))
            return Result.error(-1, "已存在菜品");
        if (cuisine.getPrice() == null || cuisine.getPrice() < 0)
            return Result.error(-1, "菜品价格错误");
        cuisineService.updateById(cuisine);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<?> delete(@NotNull @RequestBody Cuisine cuisine){
        cuisine.setStatus(1);//逻辑删除
        cuisineService.updateById(cuisine);
        return Result.success();
    }

    @GetMapping("/viewList")
    public Result<?> viewList() {
        LambdaQueryWrapper<Cuisine> cuisineWrapper = Wrappers.lambdaQuery();
        cuisineWrapper.eq(Cuisine::getStatus,0);//仅选择已激活菜品
        List<Cuisine> cuisineList = cuisineService.list(cuisineWrapper);
        return Result.success(cuisineList);
    }

    @GetMapping("/view")
    public Result<?> view(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize) {
        LambdaQueryWrapper<Cuisine> cuisineWrapper = Wrappers.lambdaQuery();
        cuisineWrapper.eq(Cuisine::getStatus,0);//仅选择已激活菜品
        Page<Cuisine> cuisinePage = cuisineService.page(new Page<>(pageNum, pageSize),cuisineWrapper);
        return Result.success(cuisinePage);
    }

    @GetMapping("/viewUsingList")
    public Result<?> viewUsingList(@RequestParam String cuisineID) {
        BigInteger input = new BigInteger(cuisineID);
        List<CuisineUsing> resList = cuisineUsingService.getListByCuisineIDWithIngredientName(input);
        return Result.success(resList);
    }

    @GetMapping("/find")
    public Result<?> find(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam String search) {
        LambdaQueryWrapper<Cuisine> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Cuisine::getStatus,0);//仅选择已激活菜品
        if (StrUtil.isNotBlank(search)) {
            if (CheckUtil.isNum(search))
                wrapper.eq(Cuisine::getID, Integer.parseInt(search));
            else
                wrapper.like(Cuisine::getName, search);
        }
        Page<Cuisine> cuisinePage = cuisineService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(cuisinePage);
    }

    @GetMapping("/findList")
    public Result<?> findList(@RequestParam String search) {
        LambdaQueryWrapper<Cuisine> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            if (CheckUtil.isNum(search))
                wrapper.eq(Cuisine::getID, Integer.parseInt(search));
            else
                wrapper.like(Cuisine::getName, search);
        }
        List<Cuisine> resList = cuisineService.list(wrapper);
        return Result.success(resList);
    }
}