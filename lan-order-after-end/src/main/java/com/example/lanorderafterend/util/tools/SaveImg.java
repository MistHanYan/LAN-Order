package com.example.lanorderafterend.util.tools;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class SaveImg {
    // 服务器图片仓库地址
    private static final String imgPath = "/home/mist/pictures/stores/";

    // 保存图片
    public static String saveImg(String imgName , MultipartFile img){

        // 创建本地文件对象
        File imgFile = new File(imgPath+imgName);
        try {
            // 将上传的文件保存到本地
            img.transferTo(imgFile);
            // 获取上传文件的相对路径
            // 返回上传文件的相对路径
            return imgPath + imgName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
