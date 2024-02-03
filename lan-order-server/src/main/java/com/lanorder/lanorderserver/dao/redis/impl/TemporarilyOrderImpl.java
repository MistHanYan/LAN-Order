package com.lanorder.lanorderserver.dao.redis.impl;

import com.lanorder.lanorderserver.common.entity.pojo.StoreList;
import com.lanorder.lanorderserver.common.entity.pojo.TabOrder;
import com.lanorder.lanorderserver.common.entity.pojo.TabStore;
import com.lanorder.lanorderserver.dao.redis.TemporarilyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TemporarilyOrderImpl implements TemporarilyOrder {
    private static final String ORDER_HASH_KEY = "temporarilyOrder";

    private static final String STORE_HASH_KEY = "temporarilyStoreList";

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public TemporarilyOrderImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void putOrder(TabOrder order) {
        redisTemplate.opsForHash().put(ORDER_HASH_KEY, String.valueOf(order.getTabNum()), order);
    }

    @Override
    public void putStoreList(String id, List<TabStore> storeList) {
        StoreList List = new StoreList();
        redisTemplate.opsForHash().put(STORE_HASH_KEY, id,List.getStoreList());
    }

    // show all order is only admin used
    @Override
    public List<TabOrder> findAllOrder() {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        List<TabOrder> tabOrdersList = new ArrayList<>();
        for (Object orderInRedis : hashOperations.entries(ORDER_HASH_KEY).values()) {
            tabOrdersList.add((TabOrder) orderInRedis);
        }
        return tabOrdersList;
    }

    @Override
    public TabOrder findOrderById(String id) {
        Object object = redisTemplate.opsForHash().get(ORDER_HASH_KEY,id);
        return (TabOrder) object;
    }


    @Override
    public void deleteOrderById(String id) {
        redisTemplate.opsForHash().delete(ORDER_HASH_KEY,id);
        redisTemplate.opsForHash().delete(STORE_HASH_KEY,id);
    }

    @Override
    public List<TabStore> findStoreListById(String id) {
        Object object = redisTemplate.opsForHash().get(STORE_HASH_KEY,id);
        StoreList storeList = (StoreList) object;
        if (storeList != null) {
            if(storeList.getStoreList().size() != 0){
                return storeList.getStoreList();
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

}