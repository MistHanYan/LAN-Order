package com.lanorder.lanorderserver.dao.redis;


import com.lanorder.lanorderserver.common.entity.pojo.TabOrder;

import java.util.Map;

public interface TemporarilyOrder {
    void putOrder(TabOrder order);

    Map<String , Object> findAllOrder();

    TabOrder findOrderById(String id);

    void deleteOrderById(String id);
}
