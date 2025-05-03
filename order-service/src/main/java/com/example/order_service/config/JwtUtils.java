// 這個檔案是用來處理 JWT Token 的工具類別，包含生成、解析和驗證 Token 的功能。
// 此工具類別提供生成、驗證和解析 JWT Token 的方法。

package com.example.order_service.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;

    // 從 Token 中取得使用者名稱
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret) // 使用密鑰解析 Token
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // 取得主體（使用者名稱）
    }

    // 驗證 Token 是否有效
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token); // 驗證簽章
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 如果解析失敗，回傳 false
        }
    }
}