package com.example.lanorderafterend.service;

import com.example.lanorderafterend.entity.DeskMsg;
import com.example.lanorderafterend.entity.Marketing;
import com.example.lanorderafterend.entity.Order;
import com.example.lanorderafterend.entity.UserLoginMsg;
import com.example.lanorderafterend.util.mybatis.TabRecord;
import com.example.lanorderafterend.util.mybatis.TabStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Admin {

    String login(UserLoginMsg userLoginMsg);

    void updateOrderByTabNum(Order order);

    Map<String,Object> getOrderList() throws JsonProcessingException;

    int isEnd(int num) throws JsonProcessingException;


    int addStore(TabStore tabStore , String imgName , MultipartFile img);

    int updateStoreById(TabStore tabStore);

    List<TabStore> seekStoreLike(String seekString);

    int deleteStoreById(int id);

    List<TabStore> getStoreList();

    int sellout(List<TabStore> storeList);

    Map<String,Object> getOnSalesStore();

    int deleteRecordById(int id);

    int updateRecordById(TabRecord tabRecord);

    int addRecord(TabRecord tabRecord);

    List<TabRecord> getRecordList();


    int addMarketingAll(Marketing marketing);

    int deleteMarketing(int id);

    String getMarketingQR(Marketing marketing) throws IOException;

    List<String> getTabList();

    int deleteTabByPath(String pathImg);

    String addTab(DeskMsg deskMsg) throws IOException;
}
