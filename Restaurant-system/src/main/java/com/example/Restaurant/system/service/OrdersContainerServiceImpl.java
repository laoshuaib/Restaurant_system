package com.example.Restaurant.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Restaurant.system.entity.OrdersContainer;
import com.example.Restaurant.system.mapper.OrdersContainerMapper;
import com.example.Restaurant.system.service.mapper.OrdersContainerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class OrdersContainerServiceImpl extends ServiceImpl<OrdersContainerMapper, OrdersContainer>
        implements OrdersContainerService {
    @Resource
    OrdersContainerMapper ordersContainerMapper;

    public List<OrdersContainer> getListByOrdersIDWithCuisineName(BigInteger ordersID){
        return ordersContainerMapper.getListByOrdersIDWithCuisineName(ordersID);
    }

    public List<BigInteger> getIDListByOrdersID(BigInteger ordersID){
        return ordersContainerMapper.getIDListByOrdersID(ordersID);
    }
}
