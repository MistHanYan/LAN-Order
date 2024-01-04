package com.example.lanorderafterend.util.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_marketing")
public class TabMarketingCode {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    @TableField(value = "code")
    private String code;

    @TableField("type")
    private String type;

    @TableField("out_value")
    private int outValue;

    @TableField("max_value")
    private int maxValue;

    @TableField("type_discount")
    private Double typeDiscount;
}
