package com.lanorder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lanorder.common.entity.pojo.Tabs;
import com.lanorder.common.entity.request.receive.DeskMsg;
import com.lanorder.common.entity.request.receive.Marketing;
import com.lanorder.common.entity.request.receive.UserLoginMsg;
import com.lanorder.common.entity.pojo.TabOrder;
import com.lanorder.common.response.ErrorEnum;
import com.lanorder.common.entity.pojo.TabRecord;
import com.lanorder.common.entity.pojo.TabStore;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Admin {

    String login(UserLoginMsg userLoginMsg);

    TabOrder updateOrderByTabNum(TabOrder order);

    Map<String,Object> getOrderList() throws JsonProcessingException;

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

    ErrorEnum deleteTabByPath(Integer id) throws JsonProcessingException;

    ErrorEnum addTab(DeskMsg deskMsg) throws IOException;
}
