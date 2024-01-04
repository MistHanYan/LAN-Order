package com.example.lanorderafterend.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Record {
    private int id;
    private LocalDateTime updatedDate; // 更新时间
    private List<RecordStore> storeList; // 商品列表
    private String remark; // 备注
}
