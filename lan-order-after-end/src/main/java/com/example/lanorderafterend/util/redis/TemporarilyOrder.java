package com.example.lanorderafterend.util.redis;

import com.example.lanorderafterend.entity.Order;

import java.util.Map;

public interface TemporarilyOrder {
    void putOrder(Order order);

    Map<String , Object> findAllOrder();

    Order findOrderById(String id);

    void deleteOrderById(String id);
}
