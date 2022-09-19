package com.example.Restaurant.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@TableName("buy")
@Data
public class Buy {
    @TableId(value = "id", type = IdType.AUTO)//自动生成
    private BigInteger ID;//不用int，避免查询出错
    @TableField(value = "userid")
    private BigInteger userID;
    @TableField(exist = false)
    private String userName;
    @TableField(value = "ingredientid")
    private BigInteger ingredientID;
    @TableField(exist = false)
    private String ingredientName;
    private Double amount;
    private Double price;
    @TableField(fill= FieldFill.INSERT)//采购记录不允许修改更新，只允许插入
    private LocalDateTime time;
}
