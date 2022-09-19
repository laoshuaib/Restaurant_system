package com.example.Restaurant.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Restaurant.system.entity.CuisineUsing;
import com.example.Restaurant.system.mapper.CuisineUsingMapper;
import com.example.Restaurant.system.service.mapper.CuisineUsingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class CuisineUsingServiceImpl extends ServiceImpl<CuisineUsingMapper, CuisineUsing> implements CuisineUsingService {
    @Resource
    CuisineUsingMapper cuisineUsingMapper;

    public List<CuisineUsing> getListByCuisineIDWithIngredientName(BigInteger cuisineID){
        return cuisineUsingMapper.getListByCuisineIDWithIngredientName(cuisineID);
    }
}
