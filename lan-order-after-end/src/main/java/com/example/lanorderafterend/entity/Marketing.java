package com.example.lanorderafterend.entity;

import lombok.Data;

import java.util.List;

@Data
public class Marketing {
    private String type; // 优惠类型
    private String code; // 优惠码
    private int id;
    private List<MaxOut> max_out; // 满减参数
    private double discount; // 打折
    private String remark; // 备注
}
