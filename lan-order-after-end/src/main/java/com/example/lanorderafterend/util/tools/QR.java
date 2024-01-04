package com.example.lanorderafterend.util.tools;

import com.example.lanorderafterend.entity.DeskMsg;
import com.example.lanorderafterend.entity.Marketing;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class QR {

    private final String tabFilePath = "/home/mist/pictures/tabs/";
    private final String maQRFilePath = "/home/mist/pictures/maQR/";
    private BufferedImage getQRCode(String data) {
        int size = 250; // 二维码图片的尺寸（宽度和高度）

        try {
            // 设置二维码的参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符集编码
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // 设置纠错级别

            // 创建二维码的比特矩阵(BitMatrix)
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, size, size, hints);

            // 创建BufferedImage对象，并根据比特矩阵(BitMatrix)绘制二维码
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, size, size);
            graphics.setColor(Color.BLACK);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (bitMatrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }
            return image;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDeskQR(DeskMsg deskMsg) throws IOException {
        // 组装WiFi连接的字符串
        String wifiConfig = "WIFI:S:" + deskMsg.getLanName() + ";T:" + deskMsg.getLanType() +
                ";P:" + deskMsg.getLanPasswd() + ";;";

        // 将WiFi连接和桌号ID的字符串组合起来
        String combinedConfig = wifiConfig + " " + deskMsg.getID();
        String path = tabFilePath+generateRandomString()+".png";
        File img = new File(path);
        ImageIO.write(Objects.requireNonNull(getQRCode(combinedConfig)), "PNG", img);
        return path;
    }

    public String getDiscountCode(Marketing discountCode) throws IOException {
        String path = maQRFilePath+generateRandomString()+".png";
        File img = new File(path);
        ImageIO.write(Objects.requireNonNull(getQRCode(new Json().toJson(discountCode))), "PNG", img);
        return path;
    }

    public static String generateRandomString() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").substring(0, 10);
    }

    public List<String> getTabAllPath() {
        try {
            Path path = Paths.get(tabFilePath);
            List<String> imageExtensions = List.of(".png"); // 可自定义支持的图片扩展名
            return Files.walk(path)
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(string -> imageExtensions.stream().anyMatch(string::endsWith))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 通过路径删除生成的图片*/
    public int deleteImgByPath(String pathImg){
        Path path = Paths.get(pathImg);
        try {
            Files.delete(path);
            return 1;
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
            return 0;
        }
    }
}
