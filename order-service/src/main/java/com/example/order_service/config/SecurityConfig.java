// 這個檔案是用來設定應用程式的安全性，包括 JWT 驗證和請求授權。
// 此類別負責配置應用程式的安全性設定，包括認證與授權。

package com.example.order_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 關閉 CSRF 保護，並要求所有請求都需要認證
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated() // 所有請求都需要 JWT 驗證
            );

        // 將自訂的 JWT 過濾器加入過濾器鏈
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // 建立並回傳 SecurityFilterChain
    }
}