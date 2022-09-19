package com.example.Restaurant.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@TableName("fee")
@Data
public class Fee {
    @TableId(value = "id", type = IdType.AUTO)//自动生成
    private BigInteger ID;
    private String type;
    private Double price;
    @TableField(fill= FieldFill.INSERT)//费用记录不允许修改更新，只允许插入
    private LocalDateTime time;
    private String note;//注释
    @TableField(value = "userid")
    private BigInteger userID;
    @TableField(exist = false)
    private String userName;
}
