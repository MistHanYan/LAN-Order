package com.example.lanorderafterend.util.redis;

import com.example.lanorderafterend.util.mybatis.TabStore;

import java.util.Map;

public interface SellOutRepository {
    void putStore(TabStore store);

    Map<String , Object> findAllStore();

    TabStore findStoreById(String id);

    void deleteStoreData();
}
