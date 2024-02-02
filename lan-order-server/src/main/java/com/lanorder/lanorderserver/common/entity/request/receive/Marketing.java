package com.lanorder.lanorderserver.common.entity.request.receive;

import lombok.Data;

import java.util.List;

@Data
public class Marketing {

    private String type; // 优惠类型
    private String code; // 优惠码
    private Integer id;
    private List<MaxOut> max_out; // 满减参数
    private Double discount; // 打折
    private String remark; // 备注
}
