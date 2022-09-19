package com.example.Restaurant.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Restaurant.system.entity.Cuisine;
import com.example.Restaurant.system.mapper.CuisineMapper;
import com.example.Restaurant.system.service.mapper.CuisineService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;


@Service
public class CuisineServiceImpl extends ServiceImpl<CuisineMapper, Cuisine> implements CuisineService {
    @Resource
    CuisineMapper cuisineMapper;

    public boolean nameCheck(@NotNull Cuisine cuisine){
        LambdaQueryWrapper<Cuisine> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Cuisine::getName,cuisine.getName());
        wrapper.eq(Cuisine::getStatus,0);
        return cuisineMapper.selectOne(wrapper) == null;
    }

    public double getPriceByID(@NotNull BigInteger cuisineID){
        LambdaQueryWrapper<Cuisine> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Cuisine::getID,cuisineID);
        return cuisineMapper.selectOne(wrapper).getPrice();
    }

    public String getNameByID(@NotNull BigInteger cuisineID){
        return cuisineMapper.selectById(cuisineID).getName();
    }
}
