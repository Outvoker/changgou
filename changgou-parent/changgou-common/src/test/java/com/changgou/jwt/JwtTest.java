package com.changgou.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author Xu Rui
 * @date 2021/2/24 13:44
 */
public class JwtTest {

    /****
     * 创建Jwt令牌
     */
    @Test
    public void testCreateJwt(){
        JwtBuilder builder= Jwts.builder()
                .setId("888")             //设置唯一编号
                .setSubject("小白")       //设置主题  可以是JSON数据
                .setIssuedAt(new Date())  //设置签发日期
                .signWith(SignatureAlgorithm.HS256,"itcast");//设置签名 使用HS256算法，并设置SecretKey(字符串)
        //构建 并返回一个字符串
        System.out.println( builder.compact() );
    }

    /***
     * 解析Jwt令牌数据
     */
    @Test
    public void testParseJwt(){
        String compactJwt="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE2MTQxNDU1NTV9.ri94ghwm3tQjXKpI719aa1Hv5fRiksL7iwj56WO3LWY";
        Claims claims = Jwts.parser().
                setSigningKey("itcast11").
                parseClaimsJws(compactJwt).
                getBody();
        System.out.println(claims);
    }


}
