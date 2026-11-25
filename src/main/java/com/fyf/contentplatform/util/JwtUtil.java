package com.fyf.contentplatform.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author fyf
 * @Description
 * @create 2026-11-23 13:02
 */
@Component
public class JwtUtil {

    //从配置文件拿到token的密钥和有效时间
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    //把密钥字符串转换成HMAC-SHA算法需要的SecretKey密钥对象
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    //构建token并返回token对象
    public String generateToken(Long userId, String username,String role) {
        Date now = new Date();
        //算出令牌过期时间
        Date expiry = new Date(now.getTime() + expiration);
        //JJWT开始构建token
        return Jwts.builder()
                .setSubject(userId + ":" + username + ":" + role)//身份信息拼在一起用冒号隔开
                .setIssuedAt(now)//token签发时间
                .setExpiration(expiry)//token过期时间
                .signWith(getKey())//用SecretKey密钥进行签名
                .compact();//信息打包生成最终token字符串
    }

    //解析并校验token，返回token里存放的载荷Claims对象
    public Claims parseToken(String token) {
        //构建解析器解析token
        return Jwts.parserBuilder()
                .setSigningKey(getKey())//用同一个SecretKey密钥校验签名
                .build()//把解析器构建完成
                .parseClaimsJws(token)//传入token，执行解析、签名校验、过期校验
                .getBody();//拿到载荷Claims（token的数据载体：sub、iat、exp、自定义claim）
    }

    //提取token里的id并返回
    public Long getUserId(String token) {
        String subject = parseToken(token).getSubject();
        return Long.valueOf(subject.split(":")[0]);
    }

    //提取token里的角色并返回
    public String getRole(String token) {
        String subject = parseToken(token).getSubject();
        return subject.split(":")[2];
    }

    //校验token是否合法有效，返回布尔值
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
