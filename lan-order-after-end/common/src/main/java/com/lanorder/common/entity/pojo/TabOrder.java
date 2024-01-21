package com.lanorder.common.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@TableName("tb_order")
public class TabOrder implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("created_date")
    private Timestamp createdDate;

    @TableField("updated_date")
    private Timestamp updatedDate;

    @TableField("store_list")
    private String storeListJson;

    @TableField("amount")
    private Double amount;

    @TableField(exist = false)
    private List<TabStore> storeList;

    @TableField(exist = false)
    private String type;

    @TableField("code")
    private String code;
    /**
     * 序列号，桌号
     */
    @TableField("tab_num")
    private Integer tabNum;

    public TabOrder() {
    }
}
