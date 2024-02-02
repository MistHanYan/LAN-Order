package com.lanorder.lanorderserver.api.service.impl;

import com.lanorder.lanorderserver.api.service.ImageService;
import com.lanorder.lanorderserver.api.util.qr.QR;
import com.lanorder.lanorderserver.common.entity.request.launch.StrategyCode;
import com.lanorder.lanorderserver.common.entity.request.launch.UpdImgPara;
import com.lanorder.lanorderserver.common.entity.request.response.UpdImgRe;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String uploadImagPath = "http://47.109.47.32:40027/api/v1/upload";

    private static final String delImagPath = "http://47.109.47.32:40027/api/v1/images";

    private static final String token = "Bearer 4|4Uo05edEa9IzBVyWcomrn9d0eZIhOJPh5pM5RkG4";


    // 创建 RestTemplate 实例
    private static final RestTemplate restTemplate = new RestTemplate();

    @Override
    public UpdImgRe putImg(MultipartFile file) {
        UpdImgPara updImgPara = new UpdImgPara();
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        assert fileName != null;
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码

        try {
            File img = File.createTempFile(QR.generateRandomString(), prefix);
            file.transferTo(img);
            updImgPara.setFile(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updImgPara.setStrategy_id(StrategyCode.STORE.getStrategy());
        return uploadImg(updImgPara);
    }


    public static UpdImgRe putImg(File file , StrategyCode strategyCode) {
        UpdImgPara updImgPara = new UpdImgPara();
        updImgPara.setFile(file);
        updImgPara.setStrategy_id(strategyCode.getStrategy());
        return uploadImg(updImgPara);
    }

    @Override
    public Boolean delImg(String key) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",token);
        String url = delImagPath+"/:"+key;
        return sendReq(url,null,HttpMethod.DELETE,httpHeaders).getStatus();
    }

    private static UpdImgRe sendReq(String reqImagPath, Object body, @NonNull HttpMethod httpMethod , HttpHeaders httpHeaders){
        HttpEntity<Object> objectHttpEntity
                = new HttpEntity<>(body, httpHeaders);

        try {
            // 发起请求并获取响应
            ResponseEntity<UpdImgRe> response = restTemplate.exchange(
                    reqImagPath, httpMethod, objectHttpEntity, UpdImgRe.class);
            // 获取响应数据
            return response.getBody();
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException();
        }

    }

    private static UpdImgRe uploadImg(UpdImgPara updImgPara) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("strategy_id", updImgPara.getStrategy_id());
        formData.add("file", new FileSystemResource(updImgPara.getFile().getPath()));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.set("Authorization",token);
        return sendReq(uploadImagPath,formData,HttpMethod.POST,httpHeaders);
    }
}
