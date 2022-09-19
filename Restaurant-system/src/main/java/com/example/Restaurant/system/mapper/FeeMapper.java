package com.example.Restaurant.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Restaurant.system.entity.Fee;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

public interface FeeMapper extends BaseMapper<Fee> {
    @Select("SELECT * FROM\n" +
            "fee\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B")
    List<Fee> getListWithUserName();

    @Select("SELECT * FROM\n" +
            "fee\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n" +
            "WHERE id = #{input}")
    List<Fee> getListByIDWithUserName(@Param("input") BigInteger input);

    @Select("SELECT * FROM\n" +
            "fee\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n" +
            "WHERE type like concat('%',#{input},'%')")
    List<Fee> getListByTypeWithUserName(@Param("input") String input);

    @Select("SELECT * FROM\n" +
            "fee\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n" +
            "WHERE user_name like concat('%',#{input},'%')")
    List<Fee> getListByUserNameWithUserName(@Param("input") String input);

    @Select("SELECT * FROM\n" +
            "fee\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n" +
            "WHERE userID = #{input}")
    List<Fee> getListByUserIDWithUserName(@Param("input") BigInteger input);
}
