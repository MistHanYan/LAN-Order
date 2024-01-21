package com.lanorder.dao.redis;



import com.lanorder.common.entity.pojo.TabStore;

import java.util.Map;

public interface SellOutRepository {
    void putStore(TabStore store);

    Map<String , Object> findAllStore();

    TabStore findStoreById(String id);

    void deleteStoreData();
}
