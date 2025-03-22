// 這個檔案是用來攔截 HTTP 請求並驗證 JWT Token 的過濾器。
// 此類別實作了一個 JWT 驗證的過濾器。
// 它會攔截請求並驗證 JWT Token，以確保安全性。

package com.example.order_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 取得 Authorization 標頭
        String header = request.getHeader("Authorization");

        // 如果標頭存在且以 "Bearer " 開頭，則進行 Token 驗證
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // 去掉 "Bearer " 前綴
            if (jwtUtils.validateToken(token)) { // 驗證 Token 是否有效
                String username = jwtUtils.getUsernameFromToken(token); // 從 Token 中取得使用者名稱

                // 建立使用者的認證資訊
                User principal = new User(username, "", Collections.emptyList());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 將認證資訊存入 SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 繼續執行過濾器鏈
        filterChain.doFilter(request, response);
    }
}