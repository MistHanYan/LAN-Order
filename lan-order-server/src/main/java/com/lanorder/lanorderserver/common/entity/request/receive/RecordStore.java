package com.lanorder.lanorderserver.common.entity.request.receive;

import lombok.Data;

@Data
public class RecordStore {
    private Integer id;
    private String name; // record name
    private Double price;
}
