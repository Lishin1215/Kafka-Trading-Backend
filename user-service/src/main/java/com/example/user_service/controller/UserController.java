package com.example.user_service.controller;

import com.example.user_service.config.JwtUtils;
import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
// @RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register") // 處理用戶註冊的 API
    public Map<String, Object> register(@RequestBody Map<String, String> req) {
        // 呼叫 UserService 的 register 方法，新增用戶
        User user = userService.register(req.get("username"), req.get("password"));
        // 建立回應 Map，包含註冊成功訊息和用戶 ID
        Map<String, Object> res = new HashMap<>();
        res.put("message", "User registered successfully"); // 註冊成功訊息
        res.put("userId", user.getId()); // 用戶 ID
        return res; // 回傳結果
    }

    @PostMapping("/login") // 處理用戶登入的 API
    public Map<String, Object> login(@RequestBody Map<String, String> req) {
        try {
            // 驗證用戶名和密碼是否正確
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.get("username"), req.get("password")));
        } catch (AuthenticationException e) {
            // 驗證失敗時，拋出例外
            throw new RuntimeException("Invalid credentials");
        }
        // 驗證成功後，生成 JWT Token
        String token = jwtUtils.generateToken(req.get("username"));
        // 建立回應 Map，包含 Token 和過期時間
        Map<String, Object> res = new HashMap<>();
        res.put("token", token); // JWT Token
        res.put("expiresIn", 3600); // Token 過期時間（秒）
        return res; // 回傳結果
    }

    @GetMapping("/balance") // 查詢用戶餘額的 API
    public Map<String, Object> balance(@RequestParam String username) {
        // 根據用戶名查詢用戶資料
        User user = userService.findByUsername(username).orElseThrow();
        // 建立回應 Map，包含用戶名和餘額
        Map<String, Object> res = new HashMap<>();
        res.put("username", user.getUsername()); // 用戶名
        res.put("balance", user.getBalance()); // 用戶餘額
        return res; // 回傳結果
    }

    @PostMapping("/update-balance") // 更新用戶餘額的 API
    public ResponseEntity<?> updateBalance(@RequestBody Map<String, Object> req) {
        // 從請求中取得用戶名和金額
        String username = (String) req.get("username");
        Double amount = ((Number) req.get("amount")).doubleValue();

        // 呼叫 UserService 更新餘額
        boolean updated = userService.updateBalance(username, amount);
        if (updated) {
            // 更新成功，回傳成功訊息
            return ResponseEntity.ok().body(Map.of("message", "Balance updated"));
        } else {
            // 用戶不存在，回傳錯誤訊息
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }
    }

}