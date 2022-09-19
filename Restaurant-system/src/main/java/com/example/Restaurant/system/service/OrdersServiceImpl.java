package com.example.Restaurant.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Restaurant.system.entity.Orders;
import com.example.Restaurant.system.mapper.OrdersMapper;
import com.example.Restaurant.system.service.mapper.OrdersService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Resource
    OrdersMapper ordersMapper;

    public List<Orders> getListWithUserName(){
        return ordersMapper.getListWithUserName();
    }

    public List<Orders> getEatInListWithUserName(){
        return ordersMapper.getListByTypeWithUserName(0);
    }

    public List<Orders> getTakeOutListWithUserName(){
        return ordersMapper.getListByTypeWithUserName(1);
    }
    public List<Orders> getListByIDWithUserName(@NotNull BigInteger ID){
        return ordersMapper.getListByIDWithUserName(ID);
    }
}
