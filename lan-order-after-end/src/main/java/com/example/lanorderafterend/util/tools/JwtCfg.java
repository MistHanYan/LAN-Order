package com.example.lanorderafterend.util.tools;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

public class JwtCfg {
    private static final String SECRET_KEY = "MistIsZhangYang1MistIsZhangYang1"; // 用于签名的密钥，请根据实际需求进行修改


    private static final Logger logger = LoggerFactory.getLogger(JwtCfg.class);

    public String discountToken(String username, int time) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 24L *3600*1000*time);

        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        // 添加自定义的声明（可选）
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("username",username);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /*public Claims extractSubject(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }*/

    public boolean checkJwtTimed(String token){
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            //logger.debug("Jwt is success for to check time");
            logger.info("This is a successful check for token");
            return true;
        } catch (Exception ex){
            return false;
        }
    }
}
