package com.example.Restaurant.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.Restaurant.system.entity.OrdersContainer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

public interface OrdersContainerMapper extends BaseMapper<OrdersContainer> {
    @Select("SELECT * FROM\n" +
            "orderscontainer\n" +
            "NATURAL JOIN \n" +
            "(SELECT ID cuisineID,name cuisine_name FROM cuisine) B\n" +
            "WHERE ordersID = #{input}")
    List<OrdersContainer> getListByOrdersIDWithCuisineName(@Param("input") BigInteger input);

    @Select("SELECT * FROM\n" +
            "orderscontainer\n" +
            "WHERE ordersID = #{input}")
    List<OrdersContainer> getListByOrdersID(@Param("input") BigInteger input);

    @Select("SELECT ID FROM\n" +
            "orderscontainer\n" +
            "WHERE ordersID = #{input}")
    List<BigInteger> getIDListByOrdersID(@Param("input") BigInteger input);
}
