package com.example.lanorderafterend.util.redis;

import com.example.lanorderafterend.entity.Store;

import java.util.Map;

public interface SellOutRepository {
    void putStore(Store store);

    Map<String , Object> findAllStore();

    Store findStoreById(String id);

    void deleteStoreData();
}
