package com.lanorder.lanorderserver.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lanorder.lanorderserver.common.entity.request.response.UpdImgRe;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    // 上传图片
    UpdImgRe putImg(MultipartFile file) throws IOException;
    // 删除图片
    Boolean delImg(String key) throws JsonProcessingException;
}
