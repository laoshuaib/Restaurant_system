package com.example.Restaurant.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Restaurant.system.entity.User;
import com.example.Restaurant.system.mapper.UserMapper;
import com.example.Restaurant.system.service.mapper.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    /**
     * @param userID 用户ID
     * @return ture 为存在，false为不存在
     */
    public boolean userIDCheck(BigInteger userID){
        return userMapper.selectById(userID) != null;
    }

    public Integer userJobCheck(BigInteger userID){
        return userMapper.selectById(userID).getJob();
    }

    public String getNameByID(BigInteger userID){
        return userMapper.selectById(userID).getName();
    }

}
