package com.lanorder.lanorderserver.api.util.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lanorder.lanorderserver.api.service.impl.ImageServiceImpl;
import com.lanorder.lanorderserver.common.entity.request.launch.StrategyCode;
import com.lanorder.lanorderserver.common.entity.request.receive.DeskMsg;
import com.lanorder.lanorderserver.common.entity.request.receive.Marketing;
import com.lanorder.lanorderserver.common.entity.request.response.UpdImgRe;
import com.lanorder.lanorderserver.common.util.json.Json;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class QR {
    private static BufferedImage getQRCode(String data) {
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

    public static UpdImgRe getDeskQR(DeskMsg deskMsg) throws IOException {
        // 组装WiFi连接的字符串
        String wifiConfig = "WIFI:S:" + deskMsg.getLanName() + ";T:" + deskMsg.getLanType() +
                ";P:" + deskMsg.getLanPasswd() + ";;";

        // 将WiFi连接和桌号ID的字符串组合起来
        String combinedConfig = wifiConfig + " " + deskMsg.getTabNum();
        File img = File.createTempFile("temp",".png");
        ImageIO.write(Objects.requireNonNull(getQRCode(combinedConfig)), "PNG", img);
        UpdImgRe updImgRe = ImageServiceImpl.putImg(img, StrategyCode.TAB);
        if(updImgRe.getStatus() && img.delete()){
            return updImgRe;
        }
        return null;
    }

    public static UpdImgRe getDiscountCode(Marketing discountCode) throws IOException {
        File img = File.createTempFile("temp",".png");
        ImageIO.write(Objects.requireNonNull(getQRCode(Json.toJson(discountCode))), "PNG", img);
        UpdImgRe updImgRe = ImageServiceImpl.putImg(img, StrategyCode.MARKETING);
        if(updImgRe.getStatus() && img.delete()) {
            return updImgRe;
        }
        return null;
    }

    public static String generateRandomString() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").substring(0, 10);
    }
}
