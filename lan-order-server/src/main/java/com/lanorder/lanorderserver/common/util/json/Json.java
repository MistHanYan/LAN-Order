package com.lanorder.lanorderserver.common.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lanorder.lanorderserver.common.entity.request.response.UpdImgRe;


public class Json {
    /**
     * 对象转json*/
    public static <T> String toJson(T object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    /*public UserLoginMsg jsonToLoginMsg(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, UserLoginMsg.class);
    }*/

    public static UpdImgRe toUpdImgRe(String upImgRe) throws JsonProcessingException {
        return new ObjectMapper().readValue(upImgRe, UpdImgRe.class);
    }
}
