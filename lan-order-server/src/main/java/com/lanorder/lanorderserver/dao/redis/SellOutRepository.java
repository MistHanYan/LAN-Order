package com.lanorder.lanorderserver.dao.redis;



import com.lanorder.lanorderserver.common.entity.pojo.TabStore;

import java.util.Map;

public interface SellOutRepository {
    void putStore(TabStore store);

    Map<String , Object> findAllStore();

    TabStore findStoreById(String id);

    void deleteStoreData();
}
