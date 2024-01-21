package com.lanorder.common.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("tb_store")
public class TabStore implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("sort")
    private String sort;

    @TableField("name")
    private String name;

    @TableField("price")
    private Double price;

    @TableField("discount")
    private Double discount;

    // 销量
    @TableField("number")
    private Integer number;

    @TableField("created_date")
    private Timestamp createdDate;

    @TableField("updated_date")
    private Timestamp updateDate;

    // 在售数量
    @TableField(exist = false)
    private Integer num;

    @TableField("url")
    private String url;

    @TableField("thumbnail_url")
    private String thumbnailUrl;

    @TableField("img_key")
    private String imgKey;

    public TabStore() {
    }

    public TabStore(String sort, String name, Double price) {
        this.sort = sort;
        this.name = name;
        this.price = price;
    }
}
