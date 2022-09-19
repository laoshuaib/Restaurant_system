package com.example.Restaurant.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Restaurant.system.entity.Fee;
import com.example.Restaurant.system.mapper.FeeMapper;
import com.example.Restaurant.system.service.mapper.FeeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class FeeServiceImpl extends ServiceImpl<FeeMapper, Fee> implements FeeService {

    @Resource FeeMapper feeMapper;

    public List<Fee> listWithName() {
        return feeMapper.getListWithUserName();
    }

    public List<Fee> listByIDWithName(BigInteger ID) {
        return feeMapper.getListByIDWithUserName(ID);
    }

    public List<Fee> listByTypeWithName(String type) {
        return feeMapper.getListByTypeWithUserName(type);
    }

    public List<Fee> listByUserIDWithName(BigInteger userID) {
        return feeMapper.getListByUserIDWithUserName(userID);
    }

    public List<Fee> listByUserNameWithName(String userName) {
        return feeMapper.getListByUserNameWithUserName(userName);
    }
}
