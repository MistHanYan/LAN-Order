package com.lanorder.lanorderserver.common.entity.request.response;

import lombok.Data;

import java.util.Map;

@Data
public class UpdImgReData {
    private String key;

    private Map<String , String> links;
}
