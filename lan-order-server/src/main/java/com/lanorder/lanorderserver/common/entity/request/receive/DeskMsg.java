package com.lanorder.lanorderserver.common.entity.request.receive;

import lombok.Data;

@Data
public class DeskMsg {
    private Integer tabNum;
    private String lanName; // Is name internet
    private String lanPasswd; // Is passwd internet
    private String lanType; // Type of connect
}
