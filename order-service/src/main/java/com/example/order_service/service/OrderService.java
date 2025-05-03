// 這個檔案是用來處理訂單的商業邏輯，包含新增訂單和查詢歷史訂單的功能。

// 這個類別是專門用來處理訂單的邏輯，像是新增訂單或查詢訂單。

package com.example.order_service.service;

import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    /**
     * 新增一筆訂單，並存到資料庫
     * @param username 使用者名稱
     * @param userId 使用者的 ID
     * @param symbol 股票代號
     * @param type 訂單類型（0: 買入, 1: 賣出）
     * @param quantity 訂單數量
     * @param price 訂單價格
     * @return 存好的訂單資料
     */
    public Order createOrder(String username, Long userId, String symbol, int type, int quantity, double price) {
        // 把訂單的資料組合起來
        Order order = Order.builder()
                .username(username)
                .userId(userId)
                .symbol(symbol)
                .type(type)
                .quantity(quantity)
                .price(price)
                .timestamp(LocalDateTime.now()) // 設定現在的時間
                .build();
        return orderRepository.save(order); // 存到資料庫
    }

    /**
     * 查詢某個使用者的所有訂單，按照時間從最近的排到最舊的
     * @param userId 使用者的 ID
     * @return 該使用者的訂單列表
     */
    public List<Order> getOrdres(Long userId) {
        return orderRepository.findByUserIdOrderByTimestampDesc(userId); // 從資料庫查詢
    }
}