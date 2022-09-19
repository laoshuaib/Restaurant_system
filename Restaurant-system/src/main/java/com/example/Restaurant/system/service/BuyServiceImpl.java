package com.example.Restaurant.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Restaurant.system.entity.Buy;
import com.example.Restaurant.system.mapper.BuyMapper;
import com.example.Restaurant.system.service.mapper.BuyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class BuyServiceImpl extends ServiceImpl<BuyMapper, Buy> implements BuyService {
    @Resource
    BuyMapper buyMapper;

    public List<Buy> listByIngredientName(String ingredientName){
        return buyMapper.getBuyListByIngredientName(ingredientName);
    }
    public List<Buy> listByIngredientID(BigInteger ingredientID){
        return buyMapper.getBuyListByIngredientID(ingredientID);
    }
    public List<Buy> listByUserName(String userName){
        return buyMapper.getBuyListByUserName(userName);
    }
    public List<Buy> listByUserID(BigInteger userID){
        return buyMapper.getBuyListByUserID(userID);
    }
    public List<Buy> listWithName(){
        return buyMapper.getBuyList();
    }
}
