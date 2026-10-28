package com.xzl.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String SIGNKEY = "thisisaverylongsecretkeythatmustbeatleastcharacters"; //密钥
    private static Long expire = 43200000L; //设置一个过期时间值

    /** 生成令牌*/
    public static String generateJwt(Map<String,Object> claims){
        // 将字符串密钥转换为符合 HS256 规范的 SecretKey 对象
        SecretKey key = Keys.hmacShaKeyFor(SIGNKEY.getBytes(StandardCharsets.UTF_8));

        String jwt = Jwts.builder()
                .signWith(key) // 直接传入 SecretKey 对象，更安全且规范
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    /** 解析令牌*/
    public static Claims parseJwt(String jwt){
        SecretKey key = Keys.hmacShaKeyFor(SIGNKEY.getBytes(StandardCharsets.UTF_8));

        // 使用 parserBuilder() 替代过时的 parser()，这是新版 JJWT 的标准写法
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // 传入 SecretKey 对象
                .build()
                .parseClaimsJws(jwt) // 使用 parseClaimsJws 替代过时的 parseClaimsJwt
                .getBody();
        return claims;
    }

}
