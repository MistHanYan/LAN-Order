package com.lanorder.common.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("tb_import_record")
public class TabRecord implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("created_date")
    private Timestamp createdDate;

    @TableField("updated_date")
    private Timestamp updatedDate;

    @TableField("store_list")
    private String storeList;

    @TableField("remark")
    private String remark;

    public TabRecord() {
    }
}
