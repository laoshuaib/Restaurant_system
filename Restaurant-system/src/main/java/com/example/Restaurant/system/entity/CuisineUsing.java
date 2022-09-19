package com.example.Restaurant.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@TableName("cuisineusing")
@Data
public class CuisineUsing {
    @TableId(value = "id", type = IdType.AUTO)//自动生成
    private BigInteger ID;
    @TableField(value = "cuisineid")
    private BigInteger cuisineID;
    @TableField(value = "ingredientid")
    private BigInteger ingredientID;
    @TableField(exist = false)
    private String ingredientName;
    private Double dosage;//单位g
}
