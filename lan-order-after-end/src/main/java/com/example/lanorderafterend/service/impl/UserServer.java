package com.example.lanorderafterend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lanorderafterend.entity.Order;
import com.example.lanorderafterend.entity.Store;
import com.example.lanorderafterend.service.User;
import com.example.lanorderafterend.util.mybatis.TabMarketingCode;
import com.example.lanorderafterend.util.mybatis.mapper.MarketingCodeMapper;
import com.example.lanorderafterend.util.redis.SellOutRepository;
import com.example.lanorderafterend.util.redis.TemporarilyOrder;
import com.example.lanorderafterend.util.tools.DiscountRule;
import com.example.lanorderafterend.util.tools.websocket.PushOrder;
import jakarta.annotation.Resource;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

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
    private MarketingCodeMapper marketingCodeMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserServer.class);

    // 发送给前端的信息
    private final static String newOrder = "NEW_ORDER";

    private final static String addOrder = "ADD_ORDER";


    /**
     * 添加订单
     * 存在超卖单线程进入
     * 会先判断是否有订单，没有新增，有就基于原有订单修改*/
    @Synchronized
    @Override
    public int addOrder(Order order) throws Exception {
        // 查找redis中是否存在这笔订单
        if(temporarilyOrder.findOrderById(String.valueOf(
                order.getTabNum())) == null){
            // 核算商品总价
            order.setTotal(taximeter(order));
            for (Store s : order.getStoreList()) {
                // 如果库存存充足将扣掉在售商品库存
                if(updateStoreNumById(String.valueOf(
                        s.getId()),
                        s.getNum()) == 1){
                    // 库存够，更新完商品更新订单
                    temporarilyOrder.putOrder(order);
                }else {
                    // 库存不足返回0
                    return 0;
                }
            }
            // 给前端发消息有新订单
            pushOrderToAdmin(newOrder);
            return 1;
        }else {
            if(goAdd(order) == 1) {
                // 给前端发消息有新订单
                pushOrderToAdmin(addOrder);
                return 1;
            }else {
                return 0;
            }
        }
    }


    // 推送订单消息给管理员
    private void pushOrderToAdmin(String msg) throws Exception {
        TextMessage textMessage = new TextMessage(msg);
        for (WebSocketSession session : pushOrder.getSessions()) {
            pushOrder.handleMessage(session, textMessage);
        }
    }

    /**
     * 追加订单
     * 避免超卖，只允许单线程进入
     * */
    @Synchronized
    private int goAdd(Order order) {
        // 取出redis中原始订单
        List<Store> storeListed = temporarilyOrder.findOrderById(
                String.valueOf(order.getTabNum())).getStoreList();

        // 用来保存新订单和原始订单重组后的订单
        List<Store> finalOrder = new ArrayList<>();

        // 取出新订单
        List<Store> newStoreList = order.getStoreList();
        for (Store s : newStoreList) {
            boolean found = false;
            for (Store t : storeListed) {
                // 如果新订单商品在原始订单中存在并且修改在售商品数量成功
                // 将修改原始订单中商品数量
                if (s.getId() == t.getId()
                        && updateStoreNumById(
                        String.valueOf(s.getId()),
                        s.getNum()) == 1) {
                    // 更新订单商品数
                    t.setNum(s.getNum() + t.getNum());
                    finalOrder.add(t);
                    found = true;
                    break;
                }
            }
            // 原始订单中不存在商品
            if (!found) {
                if (updateStoreNumById(String.valueOf(
                        s.getId()),
                        s.getNum()) == 1) {
                    // 给订单添加新商品
                    finalOrder.add(s);
                } else {
                    return 0;
                }
            }
        }

        // 更新订单中的商品
        order.setStoreList(finalOrder);
        // 核算总价
        order.setTotal(taximeter(order));
        // 更新订单
        temporarilyOrder.putOrder(order);
        return 1;
    }

    /**
     * 更新在售商品数量
     * 成功返回1
     * 库存不够返回0
     * 失败返回-1*/
    public int updateStoreNumById(String id, int num) {
        // 通过key取redis中的商品
        Store store = sellOutRepository.findStoreById(id);
        // 如果存在将尝试修改
        if (store != null) {
            if(store.getNum()-num < 0){
                logger.debug("库存不足，回档");
                return 0;
            }else {
                // 更新数量
                store.setNum(store.getNum()-num);
                // update store
                sellOutRepository.putStore(store);
                return 1;
            }
        }
        return -1;
    }


    /**
     * 计价方法
     * 通过订单中code（优惠码）核算方法
     * 在此之前会先核算所有商品的价值
     * */
    private Double taximeter(Order order) {
        double amount = 0.0;
        // 核算订单中商品的价值
        for (Store s :
                order.getStoreList()) {
            Store a = sellOutRepository.findStoreById(String.valueOf(s.getId()));
            amount += (a.getPrice() * s.getNum() * a.getDiscount());
        }
        // 如果有优惠码，检查code合法性，并取出提前定义的优惠方案
        if(order.getCode() != null){
            TabMarketingCode tabMarketingCode = getDiscountCode(order.getCode());
            return switch (tabMarketingCode.getType()) {
                case "001" -> DiscountRule.discount(amount, tabMarketingCode.getTypeDiscount());
                case "002" -> DiscountRule.maxOutOne(amount, tabMarketingCode.getMaxValue(), tabMarketingCode.getOutValue());
                case "003" -> DiscountRule.maxOutAll(amount, tabMarketingCode.getMaxValue(), tabMarketingCode.getOutValue());
                default -> amount;
            };
        }
        return amount;
    }

    /**
     * 检验订单code返回优惠实体*/
    private TabMarketingCode getDiscountCode(String code){
        QueryWrapper<TabMarketingCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",code);
        return marketingCodeMapper.selectOne(queryWrapper);
    }
}
