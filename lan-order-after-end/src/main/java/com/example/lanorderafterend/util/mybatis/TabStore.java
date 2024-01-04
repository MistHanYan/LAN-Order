package com.example.lanorderafterend.util.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_store")
public class TabStore {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField(value = "sort")
    private String sort;

    @TableField(value = "name")
    private String name;

    @TableField(value = "price")
    private double price;

    @TableField(value = "discount")
    private double discount;

    @TableField(value = "number")
    private int number;

    @TableField(value = "created_date")
    private LocalDateTime createdDate;

    @TableField(value = "updated_date")
    private LocalDateTime updateDate;
}
