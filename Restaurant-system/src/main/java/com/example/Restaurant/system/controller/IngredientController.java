package com.example.Restaurant.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.Restaurant.system.common.Result;
import com.example.Restaurant.system.entity.Buy;
import com.example.Restaurant.system.entity.Ingredient;
import com.example.Restaurant.system.service.BuyServiceImpl;
import com.example.Restaurant.system.service.IngredientServiceImpl;
import com.example.Restaurant.system.untils.CheckUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    @Resource
    IngredientServiceImpl ingredientService;
    @Resource
    BuyServiceImpl buyService;

    @PostMapping("/save")
    public Result<?> save(@NotNull @RequestBody Ingredient ingredient) {
        if (Objects.equals(ingredient.getName(), ""))
            return Result.error(-1, "食材名未填写");
        if (!ingredientService.nameCheck(ingredient))
            return Result.error(-1, "已存在该食材");
        ingredientService.save(ingredient);
        return Result.success();
    }

    @GetMapping("/view")
    public Result<?> findManagePage(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Ingredient> IngredientPage = ingredientService.page(new Page<>(pageNum, pageSize));
        return Result.success(IngredientPage);
    }

    @GetMapping("/viewList")
    public Result<?> viewList() {
        List<Ingredient> ingredients = ingredientService.list();
        return Result.success(ingredients);
    }

    @PostMapping("/update")
    public Result<?> update(@NotNull @RequestBody Ingredient ingredient) {
        if (Objects.equals(ingredient.getName(), ""))
            return Result.error(-1, "食材名未填写");
        if (!ingredientService.nameCheck(ingredient) &&
                !Objects.equals(ingredientService.getById(ingredient.getID()).getName(), ingredient.getName()))
            return Result.error(-1, "已存在该食材");
        if (ingredient.getReserves() == null || ingredient.getReserves() < 0)
            return Result.error(-1, "食材储量错误");
        ingredientService.updateById(ingredient);
        return Result.success();
    }

    @GetMapping("/find")
    public Result<?> find(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam String search) {
        LambdaQueryWrapper<Ingredient> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            if (CheckUtil.isNum(search))
                wrapper.eq(Ingredient::getID, Integer.parseInt(search));
            else
                wrapper.like(Ingredient::getName, search);
        }
        Page<Ingredient> IngredientPage = ingredientService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(IngredientPage);
    }
    @GetMapping("/findByReserves")
    public Result<?> findByReserves(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam String reservesSearch) {
        LambdaQueryWrapper<Ingredient> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(reservesSearch)) {
            if (CheckUtil.isNum(reservesSearch))
                wrapper.le(Ingredient::getReserves, Double.parseDouble(reservesSearch));
        }
        Page<Ingredient> IngredientPage = ingredientService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(IngredientPage);
    }

    @GetMapping("/findList")
    public Result<?> findList(@RequestParam String search) {
        LambdaQueryWrapper<Ingredient> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            if (CheckUtil.isNum(search))
                wrapper.eq(Ingredient::getID, Integer.parseInt(search));
            else
                wrapper.like(Ingredient::getName, search);
        }
        List<Ingredient> resList = ingredientService.list(wrapper);
        return Result.success(resList);
    }

    @PostMapping("/saveBuy")
    public Result<?> saveBuy(@NotNull @RequestBody Buy buy) {
        if (buy.getAmount() == null || buy.getAmount() < 0)
            return Result.error(-1, "食材采购量错误");
        if (buy.getPrice() == null || buy.getPrice() < 0)
            return Result.error(-1, "食材采购总价错误");
        buyService.save(buy);
        Ingredient res = ingredientService.getById(buy.getIngredientID());
        res.setReserves(res.getReserves() + buy.getAmount());
        ingredientService.updateById(res);
        return Result.success();
    }
}
