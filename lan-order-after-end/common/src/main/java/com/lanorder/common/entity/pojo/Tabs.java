package com.lanorder.common.entity.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_tabs")
public class Tabs {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("tab_num")
    private Integer tabNum;

    @TableField("img_key")
    private String imgKey;

    @TableField("url")
    private String url;

    @TableField("thumbnail_url")
    private String thumbnailUrl;

    public Tabs(Integer tabNum,String imgKey ,String url,String thumbnailUrl) {
        this.imgKey = imgKey;
        this.tabNum = tabNum;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }
}

