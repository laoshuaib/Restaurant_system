package com.example.Restaurant.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@TableName("orderscontainer")
@Data
public class OrdersContainer {
    @TableId(value = "id", type = IdType.AUTO)//自动生成
    private BigInteger ID;
    @TableField(value = "cuisineid")
    private BigInteger cuisineID;
    @TableField(exist = false)
    private String cuisineName;
    @TableField(value = "ordersid")
    private BigInteger ordersID;
    private Integer amount;//份数
}
