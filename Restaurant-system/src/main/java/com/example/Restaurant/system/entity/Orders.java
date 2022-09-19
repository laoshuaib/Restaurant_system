package com.example.Restaurant.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@TableName("orders")
@Data
public class Orders {
    @TableId(value = "id", type = IdType.AUTO)//自动生成
    private BigInteger ID;
    private Double price;
    private String customer;
    private Integer type;//0为堂食，1为外卖
    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime time;
    @TableField(value = "userid")
    private BigInteger userID;
    private Integer forcing;//0为否，1为是
    @TableField(exist = false)
    private String userName;

}
