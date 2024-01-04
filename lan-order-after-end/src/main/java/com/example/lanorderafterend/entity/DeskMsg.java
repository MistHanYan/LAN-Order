package com.example.lanorderafterend.entity;

import lombok.Data;

@Data
public class DeskMsg {
    private int ID;
    private String lanName; // Is name internet
    private String lanPasswd; // Is passwd internet
    private String lanType; // Type of connect
}
