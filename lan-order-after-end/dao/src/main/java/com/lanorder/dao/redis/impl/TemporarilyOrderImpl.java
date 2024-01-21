package com.lanorder.dao.redis.impl;

import com.lanorder.common.entity.pojo.TabOrder;
import com.lanorder.dao.redis.TemporarilyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TemporarilyOrderImpl implements TemporarilyOrder {
    private static final String HASH_KEY = "temporarilyOrder";

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public TemporarilyOrderImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void putOrder(TabOrder order) {
        redisTemplate.opsForHash().put(HASH_KEY, String.valueOf(order.getTabNum()), order);
    }

    // show all order is only admin used
    @Override
    public Map<String, Object> findAllOrder() {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(HASH_KEY);
    }

    @Override
    public TabOrder findOrderById(String id) {
        Object object = redisTemplate.opsForHash().get(HASH_KEY,id);
        return (TabOrder) object;
    }


    @Override
    public void deleteOrderById(String id) {
        redisTemplate.opsForHash().delete(HASH_KEY,id);
    }
}