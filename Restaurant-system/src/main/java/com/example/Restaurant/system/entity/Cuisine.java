package com.example.Restaurant.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@TableName("cuisine")
@Data
public class Cuisine {
    @TableId(value = "id", type = IdType.AUTO)//自动生成
    private BigInteger ID;//不用int，避免查询出错
    private String name;
    private Double price;
    private Integer status;//状态（0激活，1关闭）
}
