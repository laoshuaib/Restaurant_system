package com.example.Restaurant.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Restaurant.system.entity.Buy;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

public interface BuyMapper extends BaseMapper<Buy> {
    @Select("SELECT * FROM\n" +
            "(SELECT id ingredientID,name ingredient_name FROM ingredient) A\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n" +
            "NATURAL JOIN buy\n" +
            "WHERE ingredient_name like concat('%',#{input},'%')")
    List<Buy> getBuyListByIngredientName(@Param("input") String input);

    @Select("SELECT * FROM\n" +
            "(SELECT id ingredientID,name ingredient_name FROM ingredient) A\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n" +
            "NATURAL JOIN buy\n" +
            "WHERE ingredientID = #{input}")
    List<Buy> getBuyListByIngredientID(@Param("input") BigInteger input);

    @Select("SELECT * FROM\n" +
            "(SELECT id ingredientID,name ingredient_name FROM ingredient) A\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n" +
            "NATURAL JOIN buy")
    List<Buy> getBuyList();

    @Select("SELECT * FROM\n" +
            "(SELECT id ingredientID,name ingredient_name FROM ingredient) A\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n" +
            "NATURAL JOIN buy\n" +
            "WHERE user_name like concat('%',#{input},'%')")
    List<Buy> getBuyListByUserName(@Param("input") String input);

    @Select("SELECT * FROM\n" +
            "(SELECT id ingredientID,name ingredient_name FROM ingredient) A\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n" +
            "NATURAL JOIN buy\n" +
            "WHERE userID = #{input}")
    List<Buy> getBuyListByUserID(@Param("input") BigInteger input);
}
