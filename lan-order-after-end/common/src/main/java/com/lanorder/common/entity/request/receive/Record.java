package com.lanorder.common.entity.request.receive;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Record {

    private Integer id;
    private Timestamp updatedDate; // 更新时间
    private List<RecordStore> storeList; // 商品列表
    private String remark; // 备注
}
