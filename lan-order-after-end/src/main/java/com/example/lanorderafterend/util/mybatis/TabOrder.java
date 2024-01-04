package com.example.lanorderafterend.util.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_order")
public class TabOrder {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField("created_date")
    private LocalDateTime createdDate;

    @TableField("updated_date")
    private LocalDateTime updatedDate;

    @TableField("store_list")
    private String storeList;

    @TableField("amount")
    private double amount;
}
