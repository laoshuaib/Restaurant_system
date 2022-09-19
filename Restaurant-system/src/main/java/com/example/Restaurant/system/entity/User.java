package com.example.Restaurant.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@TableName("user")
@Data
public class User {
    @TableId(value = "id", type = IdType.AUTO)//自动生成
    private BigInteger ID;
    private String password;
    private String name;
    private Integer sex;//0为男，1为女
    private BigInteger telephone;
    private Double salary;
    private Integer job;//0为职工，1为经理
    private Integer status;//状态（0激活，1关闭）
}
