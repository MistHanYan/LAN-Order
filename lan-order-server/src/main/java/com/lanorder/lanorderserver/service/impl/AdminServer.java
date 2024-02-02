package com.lanorder.lanorderserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lanorder.lanorderserver.api.service.ImageService;
import com.lanorder.lanorderserver.api.util.qr.QR;
import com.lanorder.lanorderserver.common.entity.pojo.*;
import com.lanorder.lanorderserver.common.entity.request.receive.DeskMsg;
import com.lanorder.lanorderserver.common.entity.request.receive.Marketing;
import com.lanorder.lanorderserver.common.entity.request.receive.UserLoginMsg;
import com.lanorder.lanorderserver.common.entity.request.response.UpdImgRe;
import com.lanorder.lanorderserver.common.response.ErrorEnum;
import com.lanorder.lanorderserver.common.util.json.Json;
import com.lanorder.lanorderserver.common.util.jwt.JwtCfg;
import com.lanorder.lanorderserver.dao.mybatis.mapper.*;
import com.lanorder.lanorderserver.dao.redis.SellOutRepository;
import com.lanorder.lanorderserver.dao.redis.TemporarilyOrder;
import com.lanorder.lanorderserver.service.Admin;
import jakarta.annotation.Resource;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AdminServer implements Admin {
    @Resource
    private RecordMapper recordMapper;

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private TabsMapper tabsMapper;

    @Resource
    private MarketingCodeMapper codeMapper;

    @Resource
    private TemporarilyOrder temporarilyOrder;

    @Resource
    private SellOutRepository sellOutRepository;

    @Resource
    private ImageService imageService;

    private static final String USER_NUM = "mist";
    private static final String PASSWD = "123456";

    /**
     * 获取所有当前的订单信息
     * */
    @Override
    public Map<String, Object> getOrderList() {
        return temporarilyOrder.findAllOrder();
    }

    @Override
    public String login(UserLoginMsg userLoginMsg) {
        if (userLoginMsg.getUser_num().equals(USER_NUM)
                && userLoginMsg.getPasswd().equals(PASSWD)) {
            return JwtCfg.getToken(userLoginMsg.getUser_num(), 7);
        } else {
            return "";
        }
    }

    /**
     * 通过桌号更新订单
     * */
    @Transactional
    @Override
    public TabOrder updateOrderByTabNum(TabOrder order) {
        try {
            temporarilyOrder.putOrder(order);
            return order;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 结束订单
     * 通过传过来的座号
     * */
    @Transactional
    @Override
    public TabOrder isEnd(Integer num) {
        String id = String.valueOf(num);
        try{
            // 准备将redis中的订单转存到数据库
            TabOrder tabOrder = temporarilyOrder.findOrderById(id);
            tabOrder.setStoreListJson(Json.toJson(tabOrder.getStoreList()));
            recordSalas(tabOrder.getStoreList());
            // 通过key删除redis中的订单
            temporarilyOrder.deleteOrderById(id);
            tabOrder.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
            if(orderMapper.insert(tabOrder) > 0){
                return tabOrder;
            }else {
                throw new RuntimeException();
            }
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 更新数据库中商品销量
     * */
    @Transactional
    public void recordSalas(List<TabStore> storeList){
        for (TabStore s : storeList){
            TabStore stored = storeMapper.selectById(s.getId());
            stored.setNumber(stored.getNumber()+s.getNum());
            storeMapper.updateById(stored);
        }
    }

    /**
     * 添加新商品到数据库
     * */
    @Transactional
    @Override
    public ErrorEnum addStore(TabStore tabStore , MultipartFile img) {
        try {
            // 设置添加时间
            tabStore.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
            // 初始化折扣
            tabStore.setDiscount(1.0);
            // 保存图片
            UpdImgRe updImgRe = imageService.putImg(img);
            if (updImgRe.getData().getLinks() == null) {
                throw new RemoteException("Happened to one exception upload img");
            }
            // 初始化销售数量
            tabStore.setNumber(0);
            // 设置图片地址
            tabStore.setUrl(updImgRe.getData().getLinks().get("url"));
            tabStore.setThumbnailUrl(updImgRe.getData().getLinks().get("thumbnail_url"));
            tabStore.setImgKey(updImgRe.getData().getKey());

            return storeMapper.insert(tabStore) > 0
                    ? ErrorEnum.SUCCESS : ErrorEnum.DB_ERROR;
        } catch (Exception e) {
            return ErrorEnum.REQUEST_FAIL;
        }
    }

    /**
     * 通过id更新数据库中商品信息
     * */
    @Transactional
    @Override
    public ErrorEnum updateStoreById(TabStore tabStore) {
        tabStore.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        try {
            return storeMapper.updateById(tabStore) > 0
                    ? ErrorEnum.SUCCESS : ErrorEnum.DB_ERROR;
        }catch (Exception e){
           return ErrorEnum.REQUEST_FAIL;
        }
    }

    /**
     * 模糊查询数据库中name字段商品
     * */
    @ReadOnlyProperty
    @Override
    public List<TabStore> seekStoreLike(String seekString) {
        return storeMapper.selectList(new QueryWrapper<TabStore>().like("name",seekString));
    }

    /**
     * 通过id删除数据库中商品
     * */
    @Transactional
    @Override
    public ErrorEnum deleteStoreById(Integer id) {

        try {
            if(imageService
                    .delImg(storeMapper
                            .selectById(id)
                            .getImgKey())){
                return storeMapper.deleteById(id) > 0
                        ? ErrorEnum.SUCCESS : ErrorEnum.DB_ERROR;
            }else {
                throw new RuntimeException();
            }
        }catch (Exception e){
            return ErrorEnum.REQUEST_FAIL;
        }
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
     * 添加到redis
     * */
    @Transactional
    @Override
    public ErrorEnum sellout(List<TabStore> storeList) {

        try {
            // 更新前删除redis中的所有商品
            sellOutRepository.deleteStoreData();
        } catch (Exception e) {
            throw new RuntimeException("store delete to redis find exception");
        }

        try {
            // Put store to redis
            storeList.forEach(store -> sellOutRepository.putStore(store));
            return ErrorEnum.SUCCESS;
        }catch (Exception e){
            return ErrorEnum.DATA_ERROR;
        }
    }

    /**
     * 获取在售商品列表
     * */
    @ReadOnlyProperty
    @Override
    public Map<String, Object> getOnSalesStore(){
        return sellOutRepository.findAllStore();
    }

    /**
     * 通过id删除进货记录
     * */
    @Transactional
    @Override
    public ErrorEnum deleteRecordById(Integer id) {
        try {
            recordMapper.deleteById(id);
            return ErrorEnum.SUCCESS;
        }catch (Exception e){
            return ErrorEnum.DB_ERROR;
        }
    }

    /**
     * 通过id更新进货记录
     * */
    @Transactional
    @Override
    public ErrorEnum updateRecordById(TabRecord tabRecord) {
        // 设置进货信息跟新时间
        tabRecord.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
        try {
            recordMapper.updateById(tabRecord);
            return ErrorEnum.SUCCESS;
        }catch (Exception e){
            return ErrorEnum.DB_ERROR;
        }
    }

    /**
     * 添加进货记录
     * */
    @Override
    public ErrorEnum addRecord(TabRecord tabRecord){
        // 设置进货记录创建时间
        tabRecord.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));

        try {
            recordMapper.insert(tabRecord);
            return ErrorEnum.SUCCESS;
        }catch (Exception e){
            return ErrorEnum.DB_ERROR;
        }
    }

    /**
     * 获取所有进货信息列表
     * */
    @ReadOnlyProperty
    @Override
    public List<TabRecord> getRecordList() {
        try {
            return recordMapper.selectList(null);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 通过商品id添加优惠
     * */
    @Transactional
    @Override
    public ErrorEnum addMarketingAll(Marketing marketing) {
        TabStore tabStore = new TabStore();
        tabStore.setId(marketing.getId());
        tabStore.setDiscount(marketing.getDiscount());

        try {
            storeMapper.updateById(tabStore);
            return ErrorEnum.SUCCESS;
        }catch (Exception e){
            throw new RuntimeException("One Exception happen to method: add marketing");
        }
    }

    /**
     * 删除商品的优惠，商品折扣初始化为 1
     * */
    @Transactional
    @Override
    public ErrorEnum deleteMarketing(Integer id) {
        TabStore tabStore = new TabStore();
        tabStore.setId(id);
        // 重置折扣
        tabStore.setDiscount(1.00);

        try {
            storeMapper.updateById(tabStore);
            return ErrorEnum.SUCCESS;
        }catch (Exception e){
            throw new RuntimeException("One Exception happen to method: delete marketing");
        }
    }

    /**
     * 前往生成订单折扣码
     * */
    @Transactional
    @Override
    public ErrorEnum getMarketingQR(Marketing marketing) {
        if(marketing.getType().equals("001") || marketing.getType().equals("002")
                || marketing.getType().equals("003")){
            try {
                TabMarketingCode tabMarketingCode = new TabMarketingCode(
                        QR.generateRandomString()
                        ,marketing.getType()
                        ,marketing.getMax_out().get(0).getMax()
                        ,marketing.getMax_out().get(0).getOut()
                        ,marketing.getDiscount()
                );

                UpdImgRe updImgRe = QR.getDiscountCode(marketing);
                assert updImgRe != null;
                tabMarketingCode.setImgKey(updImgRe.getData().getKey());
                tabMarketingCode.setUrl(updImgRe.getData().getLinks().get("url"));
                tabMarketingCode.setThumbnailUrl(updImgRe.getData().getLinks().get("thumbnail_url"));
                // 如果优惠方案添加到数据库成功将生成优惠码
                if(codeMapper.insert(tabMarketingCode) != 1){
                    throw new RuntimeException("");
                }else {
                    return ErrorEnum.SUCCESS;
                }
            }catch (Exception e){
                return ErrorEnum.DB_ERROR;
            }

        }else {
            return ErrorEnum.DATA_ERROR;
        }
    }

    /**
     * 获取已有的桌子二维码
     * 将返回地址列表
     * */
    @Override
    public List<Tabs> getTabList() {
        try {
            return tabsMapper.selectList(null);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 通过地址删除桌子二维码
     * */
    @Transactional
    @Override
    public ErrorEnum deleteTabByPath(Integer id) {
        try{
            Tabs tabTabs = tabsMapper.selectById(id);
            if(tabsMapper.deleteById(id) != 1){
                throw new RuntimeException("删除桌面在数据库中数据发生异常");
            }
            if(!imageService.delImg(tabTabs.getImgKey())){
                throw new RuntimeException(
                        "Happened to one error at delete img of map depot");
            }
            return ErrorEnum.SUCCESS;
        }catch (Exception e){
            return ErrorEnum.DATA_NOT_FOUND;
        }
    }

    /**
     * 添加桌子二维码
     * 将连接信息封装到二维码
     * */
    @Transactional
    @Override
    public ErrorEnum addTab(DeskMsg deskMsg) {
        try {
            UpdImgRe updImgRe = QR.getDeskQR(deskMsg);
            Tabs tabTabs1 = null;
            if (updImgRe != null) {
                tabTabs1 = new Tabs(
                        deskMsg.getTabNum()
                        ,updImgRe.getData().getKey()
                        , updImgRe.getData().getLinks().get("url")
                        , updImgRe.getData().getLinks().get("thumbnail_url")
                );
            }
            tabsMapper.insert(tabTabs1);
            return ErrorEnum.SUCCESS;
        }catch (Exception e){
            return ErrorEnum.REQUEST_FAIL;
        }
    }
}
