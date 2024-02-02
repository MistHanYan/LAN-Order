package com.lanorder.lanorderserver.common.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_marketing")
public class TabMarketingCode {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "code")
    private String code;

    @TableField("type")
    private String type;

    @TableField("out_value")
    private Integer outValue;

    @TableField("max_value")
    private Integer maxValue;

    @TableField("type_discount")
    private Double typeDiscount;

    @TableField("url")
    private String url;

    @TableField("thumbnail_url")
    private String thumbnailUrl;

    @TableField("img_key")
    private String imgKey;

    public TabMarketingCode(String code, String type, Integer outValue, Integer maxValue, Double typeDiscount) {
        this.code = code;
        this.type = type;
        this.outValue = outValue;
        this.maxValue = maxValue;
        this.typeDiscount = typeDiscount;
    }
}
