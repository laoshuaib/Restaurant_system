package com.example.Restaurant.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Restaurant.system.entity.CuisineUsing;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

public interface CuisineUsingMapper extends BaseMapper<CuisineUsing> {
    @Select("SELECT * FROM\n" +
            "cuisineusing\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID ingredientID,name ingredient_name FROM ingredient) B\n" +
            "WHERE cuisineID = #{input}")
    List<CuisineUsing> getListByCuisineIDWithIngredientName(@Param("input") BigInteger input);
}
