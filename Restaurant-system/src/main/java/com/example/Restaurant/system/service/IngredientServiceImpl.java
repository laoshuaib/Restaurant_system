package com.example.Restaurant.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Restaurant.system.entity.Ingredient;
import com.example.Restaurant.system.mapper.IngredientMapper;
import com.example.Restaurant.system.service.mapper.IngredientService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class IngredientServiceImpl extends ServiceImpl<IngredientMapper, Ingredient> implements IngredientService {
    @Resource
    IngredientMapper ingredientMapper;

    public boolean nameCheck(@NotNull Ingredient ingredient){
        LambdaQueryWrapper<Ingredient> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Ingredient::getName,ingredient.getName());
        return ingredientMapper.selectOne(wrapper) == null;
    }
    public String getNameByID(BigInteger ingredientID){
        return ingredientMapper.selectById(ingredientID).getName();
    }

    public List<BigInteger> getIDByName (String name) {
        return ingredientMapper.getIDList(name);
    }
}
