package com.example.lanorderafterend.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Order implements Serializable {
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 商品列表，下单的具体商品
     */
    private List<Store> storeList;

    private String type;

    private String code;
    /**
     * 序列号，桌号
     */
    private int tabNum;
    /**
     * 总价
     */
    private double total;
}
