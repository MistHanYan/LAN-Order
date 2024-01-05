package com.example.lanorderafterend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.lanorderafterend.entity.DeskMsg;
import com.example.lanorderafterend.entity.Marketing;
import com.example.lanorderafterend.entity.Order;
import com.example.lanorderafterend.service.Admin;
import com.example.lanorderafterend.util.mybatis.TabMarketingCode;
import com.example.lanorderafterend.util.mybatis.TabOrder;
import com.example.lanorderafterend.util.mybatis.TabRecord;
import com.example.lanorderafterend.util.mybatis.TabStore;
import com.example.lanorderafterend.util.mybatis.mapper.MarketingCodeMapper;
import com.example.lanorderafterend.util.mybatis.mapper.OrderMapper;
import com.example.lanorderafterend.util.mybatis.mapper.RecordMapper;
import com.example.lanorderafterend.util.mybatis.mapper.StoreMapper;
import com.example.lanorderafterend.util.redis.SellOutRepository;
import com.example.lanorderafterend.util.redis.TemporarilyOrder;
import com.example.lanorderafterend.util.tools.Json;
import com.example.lanorderafterend.util.tools.QR;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.example.lanorderafterend.util.tools.SaveImg.saveImg;

@Service
public class AdminServer implements Admin {
    @Resource
    private RecordMapper recordMapper;

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private MarketingCodeMapper codeMapper;

    @Resource
    private TemporarilyOrder temporarilyOrder;

    @Resource
    private SellOutRepository sellOutRepository;

    private static final Logger logger = LoggerFactory.getLogger(AdminServer.class);


    /**
     * 获取所有当前的订单信息
     * */
    @Override
    public Map<String, Object> getOrderList() throws JsonProcessingException {
        Map<String,Object> orders = temporarilyOrder.findAllOrder();
        logger.debug("orders : {}",new Json().toJson(orders));
        return orders;
    }

    /**
     * 通过桌号更新订单
     * */
    @Override
    public void updateOrderByTabNum(Order order) {
        temporarilyOrder.putOrder(order);
    }

    /**
     * 结束订单
     * 通过传过来的座号
     * */
    @Override
    public int isEnd(int num) throws JsonProcessingException {
        String id = String.valueOf(num);
        Order order = temporarilyOrder.findOrderById(id);
        // 通过key删除redis中的订单
        temporarilyOrder.deleteOrderById(id);

        // 准备将redis中的订单转存到数据库
        TabOrder tabOrder = new TabOrder();
        tabOrder.setCreatedDate(LocalDateTime.now());
        tabOrder.setAmount(order.getTotal());
        tabOrder.setStoreList(new Json().toJson(order));
        recordSalas(order);
        return orderMapper.insert(tabOrder);
    }

    /**
     * 更新数据库中商品销量
     * */
    private void recordSalas(Order order){
        for (TabStore s : order.getStoreList()){
            TabStore stored = storeMapper.selectById(s.getId());
            stored.setNumber(stored.getNumber()+s.getNumber());
            storeMapper.updateById(stored);
        }
    }

    /**
     * 添加新商品到数据库
     * */
    @Override
    public int addStore(TabStore tabStore , String imgName , MultipartFile img) {
        // 设置添加时间
        tabStore.setCreatedDate(LocalDateTime.now());
        // 初始化折扣
        tabStore.setDiscount(1.0);
        // 保存图片
        String imgSavedPath = saveImg(img.getOriginalFilename(), img);
        if (imgSavedPath.equals("")) {
            return -1;
        }
        // 初始化销售数量
        tabStore.setNumber(0);
        // 设置图片地址
        tabStore.setImgPath(imgSavedPath);
        return storeMapper.insert(tabStore);
    }

    /**
     * 通过id更新数据库中商品信息
     * */
    @Override
    public int updateStoreById(TabStore tabStore) {
        tabStore.setUpdateDate(LocalDateTime.now());
        return storeMapper.updateById(tabStore);
    }

    /**
     * 模糊查询数据库中name字段商品*/
    @Override
    public List<TabStore> seekStoreLike(String seekString) {
        return storeMapper.selectList(new QueryWrapper<TabStore>().like("name",seekString));
    }

    /**
     * 通过id删除数据库中商品
     * */
    @Override
    public int deleteStoreById(int id) {
        return storeMapper.deleteById(id);
    }

    /**
     * 获取数据库中商品列表
     * */
    @Override
    public List<TabStore> getStoreList() {
        return storeMapper.selectList(null);
    }

    /**
     * 沽清功能
     * 将数据库中获取到的商品设置需要销售的数量
     * 添加到redis*/
    @Override
    public int sellout(List<TabStore> storeList) {
        // 更新前删除redis中的所有商品
        sellOutRepository.deleteStoreData();
        logger.debug("sellOut init");
        // 添加到redis
        for (TabStore store : storeList){
            sellOutRepository.putStore(store);
            logger.info("save store : {}",store);
        }
        return 1;
    }

    /**
     * 获取在售商品列表
     * */
    @Override
    public Map<String, Object> getOnSalesStore(){
        return sellOutRepository.findAllStore();
    }

    /**
     * 通过id删除进货记录
     * */
    @Override
    public int deleteRecordById(int id) {
        return recordMapper.deleteById(id);
    }

    /**
     * 通过id更新进货记录*/
    @Override
    public int updateRecordById(TabRecord tabRecord) {
        // 设置进货信息跟新时间
        tabRecord.setUpdatedDate(LocalDateTime.now());
        return recordMapper.updateById(tabRecord);
    }

    /**
     * 添加进货记录
     * */
    @Override
    public int addRecord(TabRecord tabRecord){
        // 设置进货记录创建时间
        tabRecord.setCreatedDate(LocalDateTime.now());
        return recordMapper.insert(tabRecord);
    }

    /**
     * 获取所有进货信息列表*/
    @Override
    public List<TabRecord> getRecordList() {
        return recordMapper.selectList(null);
    }

    /**
     * 通过商品id添加优惠
     * */
    @Override
    public int addMarketingAll(Marketing marketing) {
        TabStore tabStore = new TabStore();
        tabStore.setId(marketing.getId());
        tabStore.setDiscount(marketing.getDiscount());
        return storeMapper.updateById(tabStore);
    }

    /**
     * 删除商品的优惠，商品折扣初始化为 1
     * */
    @Override
    public int deleteMarketing(int id) {
        TabStore tabStore = new TabStore();
        tabStore.setId(id);
        // 重置折扣
        tabStore.setDiscount(1.00);
        return storeMapper.updateById(tabStore);
    }

    /**
     * 前往生成订单折扣码
     * */
    @Override
    public String getMarketingQR(Marketing marketing) throws IOException {
        if(marketing.getType().equals("001") || marketing.getType().equals("002") || marketing.getType().equals("003")){
            TabMarketingCode tabMarketingCode = new TabMarketingCode();
            tabMarketingCode.setCode(QR.generateRandomString());
            tabMarketingCode.setType(marketing.getType());
            tabMarketingCode.setMaxValue(marketing.getMax_out().get(0).getMax());
            tabMarketingCode.setOutValue(marketing.getMax_out().get(0).getOut());
            tabMarketingCode.setTypeDiscount(marketing.getDiscount());
            // 如果优惠方案添加到数据库成功将生成优惠码
            if(codeMapper.insert(tabMarketingCode) == 1){
                return new QR().getDiscountCode(marketing);
            }else {
                return "";
            }
        }else {
            return "";
        }
    }

    /**
     * 获取已有的桌子二维码
     * 将返回地址列表
     * */
    @Override
    public List<String> getTabList() {
        return new QR().getTabAllPath();
    }

    /**
     * 通过地址删除桌子二维码
     * */
    @Override
    public int deleteTabByPath(String pathImg) {
        return new QR().deleteImgByPath(pathImg);
    }

    /**
     * 添加桌子二维码
     * 将连接信息封装到二维码
     * */
    @Override
    public String addTab(DeskMsg deskMsg) throws IOException {
        return new QR().getDeskQR(deskMsg);
    }
}
