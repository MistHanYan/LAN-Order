package com.example.lanorderafterend.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Store implements Serializable {
    private int id;
    private String name;
    private double price;
    private String sort;
    private double discount;
    private int salesVolume;
    private int num;
}
