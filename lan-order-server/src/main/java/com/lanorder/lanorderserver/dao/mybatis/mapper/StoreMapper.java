package com.lanorder.lanorderserver.dao.mybatis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lanorder.lanorderserver.common.entity.pojo.TabStore;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper extends BaseMapper<TabStore> {
}
