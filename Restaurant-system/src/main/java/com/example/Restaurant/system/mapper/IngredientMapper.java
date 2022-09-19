package com.example.Restaurant.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Restaurant.system.entity.Ingredient;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

public interface IngredientMapper extends BaseMapper<Ingredient> {
    @Select("SELECT id FROM ingredient WHERE name like concat('%',#{input},'%')")
    List<BigInteger> getIDList(@Param("input") String input);
}
