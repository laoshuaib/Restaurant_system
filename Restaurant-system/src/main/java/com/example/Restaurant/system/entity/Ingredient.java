package com.example.Restaurant.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@TableName("ingredient")
@Data
public class Ingredient {
    @TableId(value = "id", type = IdType.AUTO)//自动生成
    private BigInteger ID;
    private String name;
    private Double reserves;//储量单位kg

}
