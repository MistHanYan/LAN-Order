package com.lanorder.lanorderserver.service;

import com.lanorder.lanorderserver.common.entity.pojo.TabOrder;
import com.lanorder.lanorderserver.common.entity.pojo.TabRecord;
import com.lanorder.lanorderserver.common.entity.pojo.TabStore;
import com.lanorder.lanorderserver.common.entity.pojo.Tabs;
import com.lanorder.lanorderserver.common.entity.request.receive.DeskMsg;
import com.lanorder.lanorderserver.common.entity.request.receive.Marketing;
import com.lanorder.lanorderserver.common.entity.request.receive.UserLoginMsg;
import com.lanorder.lanorderserver.common.response.ErrorEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Admin {

    String login(UserLoginMsg userLoginMsg);

    TabOrder updateOrderByTabNum(TabOrder order);


    // Get to list for all order
    List<TabOrder> getOrderListAll();

    // Get to order by table number
    TabOrder getOrderByTabNum(String TabNum);

    // Get order list for limit
    List<TabStore> getStoreList(String tabNum);

    TabOrder isEnd(Integer num);


    ErrorEnum addStore(TabStore tabStore , MultipartFile img) throws IOException;

    ErrorEnum updateStoreById(TabStore tabStore);

    List<TabStore> seekStoreLike(String seekString);

    ErrorEnum deleteStoreById(Integer id);

    List<TabStore> getStoreList();

    ErrorEnum sellout(List<TabStore> storeList);

    Map<String,Object> getOnSalesStore();

    ErrorEnum deleteRecordById(Integer id);

    ErrorEnum updateRecordById(TabRecord tabRecord);

    ErrorEnum addRecord(TabRecord tabRecord);

    List<TabRecord> getRecordList();


    ErrorEnum addMarketingAll(Marketing marketing);

    ErrorEnum deleteMarketing(Integer id);

    ErrorEnum getMarketingQR(Marketing marketing) throws IOException;

    List<Tabs> getTabList();

    ErrorEnum deleteTabByPath(Integer id);

    ErrorEnum addTab(DeskMsg deskMsg) throws IOException;
}
