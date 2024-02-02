package com.lanorder.lanorderserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lanorder.lanorderserver.common.entity.pojo.TabMarketingCode;
import com.lanorder.lanorderserver.common.entity.pojo.TabOrder;
import com.lanorder.lanorderserver.common.entity.pojo.TabStore;
import com.lanorder.lanorderserver.common.response.ErrorEnum;
import com.lanorder.lanorderserver.common.util.count.DiscountRule;
import com.lanorder.lanorderserver.dao.mybatis.mapper.MarketingCodeMapper;
import com.lanorder.lanorderserver.dao.mybatis.mapper.StoreMapper;
import com.lanorder.lanorderserver.dao.redis.SellOutRepository;
import com.lanorder.lanorderserver.dao.redis.TemporarilyOrder;
import com.lanorder.lanorderserver.service.User;
import com.lanorder.lanorderserver.websocket.PushOrder;
import jakarta.annotation.Resource;
import lombok.Synchronized;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServer implements User {

    @Resource
    private TemporarilyOrder temporarilyOrder;

    @Resource
    private SellOutRepository sellOutRepository;

    @Resource
    private PushOrder pushOrder;

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private MarketingCodeMapper marketingCodeMapper;

    // 发送给前端的信息
    private final static String newOrder = "NEW_ORDER";

    private final static String addOrder = "ADD_ORDER";


    /**
     * 添加订单
     * 存在超卖单线程进入
     * 会先判断是否有订单，没有新增，有就基于原有订单修改
     */
    @Synchronized
    @Transactional
    @Override
    public TabOrder addOrder(TabOrder order) {

        try {
            // 通过前端传过来的订单中商品id在数据库中找出具体商品后重新赋值
            order.setStoreList(storeInputOrder(order.getStoreList()));
            // 查找redis中是否存在这笔订单
            if (temporarilyOrder.findOrderById(
                    String.valueOf(order.getTabNum())) == null) {

                // 更新在售商品库存
                if (updateSalesNum(order.getStoreList())
                        .equals(ErrorEnum.SUCCESS)) {
                    // 核算商品总价
                    order.setAmount(taximeter(order));
                    order.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
                    temporarilyOrder.putOrder(order);

                    // 给前端发消息有新订单
                    pushOrderToAdmin(newOrder);
                    return temporarilyOrder.findOrderById(order.getTabNum().toString());
                }
                throw new RuntimeException();
            } else {
                if (goAdd(order)) {
                    // 给前端发消息有新订单
                    pushOrderToAdmin(addOrder);
                    return temporarilyOrder.findOrderById(order.getTabNum().toString());
                } else {
                    throw new RuntimeException();
                }
            }
        }catch (Exception e) {
            return null;
        }
    }

    private TabStore isEmptyStoreInDatabase(TabStore store){
        int num = store.getNum();
        store = storeMapper.selectById(store.getId());
        store.setNum(num);
        if(store.getId() == null){
            throw new RuntimeException();
        }
        return store;
    }

    private List<TabStore> storeInputOrder(List<TabStore> storeList){
        List<TabStore> list = new ArrayList<>();
        for (TabStore store: storeList) {
            list.add(isEmptyStoreInDatabase(store));
        }
        return list;
    }

    private ErrorEnum updateSalesNum(List<TabStore> storeList) {
        try {
            for (TabStore s : storeList) {
                // 如果库存存充足将扣掉在售商品库存
                if (!updateStoreNumById(String.valueOf(
                        s.getId()), s.getNum())) {
                    // 库存够，更新完商品更新订单
                    throw new RuntimeException("超卖，事务将回滚");
                }
            }
            return ErrorEnum.SUCCESS;
        }catch (Exception e){
            return ErrorEnum.OP_CONFLICT;
        }
    }


    // 推送订单消息给管理员
    private void pushOrderToAdmin(String msg) {
        pushOrder.sendAllMessage(msg);
    }

    /**
     * - 追加订单
     * - 避免超卖，只允许单线程进入
     */
    @Synchronized
    private boolean goAdd(TabOrder order) {
        List<TabStore> finalOrder = updateOrder(
                temporarilyOrder.findOrderById(
                        String.valueOf(order.getTabNum())).getStoreList()
                , order.getStoreList());
        if (finalOrder != null){
            // 更新订单中的商品
            order.setStoreList(finalOrder);
            // 核算总价
            order.setAmount(taximeter(order));
            // 更新订单
            temporarilyOrder.putOrder(order);
            return true;
        }
        return false;
    }
    
    private List<TabStore> updateOrder(List<TabStore> storeListed
            , List<TabStore> newStoreList) {
        // 用来保存新订单和原始订单重组后的订单
        List<TabStore> finalOrder = new ArrayList<>();

        for (TabStore s : newStoreList) {
            if(isEmptyStoreInOldOrder(s,storeListed)){
                finalOrder = updateStoreNumInOrder(storeListed,s);
            }else {
                finalOrder = addTabStoresToFinalOrder(s);
            }
            // 原始订单中不存在商品
        }
        return finalOrder;
    }

    private List<TabStore> addTabStoresToFinalOrder(TabStore s) {

        List<TabStore> finalOrder = new ArrayList<>();
            if (updateStoreNumById(String.valueOf(
                            s.getId()),
                    s.getNum())) {
                // 给订单添加新商品
                finalOrder.add(s);
            }
        return finalOrder;
    }

    private List<TabStore> updateStoreNumInOrder(List<TabStore> storeListed
            , TabStore s) {
        int i = 0;
        for (TabStore t : storeListed) {
            // 如果新订单商品在原始订单中存在并且修改在售商品数量成功
            // 将修改原始订单中商品数量
            if (s.getId().equals(t.getId())
                    && updateStoreNumById(
                    String.valueOf(s.getId()),
                    s.getNum())) {
                // 更新订单商品数
                t.setNum(s.getNum() + t.getNum());
                storeListed.remove(i);
                storeListed.add(t);
                return storeListed;
            }
            i++;
        }
        return storeListed;
    }

    private boolean isEmptyStoreInOldOrder(TabStore store, List<TabStore> oldOrder){
        for (TabStore s:
             oldOrder) {
            if(s.getId().equals(store.getId())){
                return true;
            }
        }
        return false;
    }

    /**
     * 更新在售商品数量
     * 成功返回1
     * 库存不够返回0
     * 失败返回-1
     */
    private boolean updateStoreNumById(String id, int num) {
        try{
            // 通过key取redis中的商品
            TabStore store = sellOutRepository.findStoreById(id);
            // 如果存在将尝试修改
            if (store != null) {
                if (store.getNum() - num < 0) {
                    throw new RuntimeException();// 超卖异常
                } else {
                    // 更新数量
                    store.setNum(store.getNum() - num);
                    // update store
                    sellOutRepository.putStore(store);
                    return true;
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }


    /**
     * 计价方法
     * 通过订单中code（优惠码）核算方法
     * 在此之前会先核算所有商品的价值
     */
    private Double taximeter(TabOrder order) {
        double amount = 0.0;
        // 核算订单中商品的价值
        amount = getAmount(order, amount);
        // 如果有优惠码，检查code合法性，并取出提前定义的优惠方案
        if (order.getCode() != null) {
            return getAfterDiscount(order, amount);
        }
        return amount;
    }

    private Double getAfterDiscount(TabOrder order, double amount) {
        TabMarketingCode tabMarketingCode = getDiscountCode(order.getCode());
        if(tabMarketingCode == null){
            return amount;
        }
        return switch (tabMarketingCode.getType()) {
            case "001" -> DiscountRule.discount(amount, tabMarketingCode.getTypeDiscount());
            case "002" ->
                    DiscountRule.maxOutOne(amount, tabMarketingCode.getMaxValue(), tabMarketingCode.getOutValue());
            case "003" ->
                    DiscountRule.maxOutAll(amount, tabMarketingCode.getMaxValue(), tabMarketingCode.getOutValue());
            default -> amount;
        };
    }

    private double getAmount(TabOrder order, double amount) {
        for (TabStore s :
                order.getStoreList()) {
            TabStore a = sellOutRepository.findStoreById(String.valueOf(s.getId()));
            amount += (a.getPrice() * s.getNum() * a.getDiscount());
        }
        return amount;
    }

    /**
     * 检验订单code返回优惠实体
     */
    private TabMarketingCode getDiscountCode(String code) {
        QueryWrapper<TabMarketingCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        return marketingCodeMapper.selectOne(queryWrapper);
    }
}
