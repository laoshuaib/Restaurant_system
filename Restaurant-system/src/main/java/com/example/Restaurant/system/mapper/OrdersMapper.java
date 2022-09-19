package com.example.Restaurant.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Restaurant.system.entity.Orders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

public interface OrdersMapper extends BaseMapper<Orders> {
    @Select("SELECT * FROM\n" +
            "orders\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B")
    List<Orders> getListWithUserName();

    @Select("SELECT * FROM\n" +
            "orders\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n"+
            "WHERE type = #{input}")
    List<Orders> getListByTypeWithUserName(@Param("input") Integer input);

    @Select("SELECT * FROM\n" +
            "orders\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID userID,name user_name FROM `user`) B\n" +
            "WHERE ID = #{input}")
    List<Orders> getListByIDWithUserName(@Param("input") BigInteger input);
}
