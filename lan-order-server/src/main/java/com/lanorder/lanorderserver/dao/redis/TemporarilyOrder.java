package com.lanorder.lanorderserver.dao.redis;


import com.lanorder.lanorderserver.common.entity.pojo.TabOrder;
import com.lanorder.lanorderserver.common.entity.pojo.TabStore;

import java.util.List;

public interface TemporarilyOrder {
    void putOrder(TabOrder order);

    void putStoreList(String TabNum, List<TabStore> storeList);

    List<TabOrder> findAllOrder();

    TabOrder findOrderById(String id);

    void deleteOrderById(String id);

    List<TabStore> findStoreListById(String tabNum);
}
