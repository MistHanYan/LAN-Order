package com.example.lanorderafterend.util.redis.impl;

import com.example.lanorderafterend.util.mybatis.TabStore;
import com.example.lanorderafterend.util.redis.SellOutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class SellOutRepositoryImpl implements SellOutRepository {

    private static final String HASH_KEY = "Stores";

    private final RedisTemplate<String,Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SellOutRepositoryImpl.class);

    @Autowired
    public SellOutRepositoryImpl(RedisTemplate<String , Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void putStore(TabStore store) {
        String storeId = String.valueOf(store.getId());
        logger.info("store:{} sell out ing ...",store);
        redisTemplate.opsForHash().put(HASH_KEY , storeId , store);
    }

    @Override
    public Map<String, Object> findAllStore() {
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(HASH_KEY);
    }

    @Override
    public TabStore findStoreById(String id) {
        return (TabStore) redisTemplate.opsForHash().get(HASH_KEY , id);
    }

    @Override
    public void deleteStoreData() {
        redisTemplate.delete(HASH_KEY);
    }
}
