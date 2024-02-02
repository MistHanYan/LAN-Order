package com.lanorder.lanorderserver.common.entity.request.launch;

import lombok.Data;

import java.io.File;

@Data
public class UpdImgPara {
    private File file;
    private Integer strategy_id;


    public UpdImgPara() {
    }
}
