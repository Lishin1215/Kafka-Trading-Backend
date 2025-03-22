// 此類別代表訂單實體，對應到資料庫中的訂單表。

package com.example.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="orders") // 'order' 是關鍵字，避免與資料庫衝突
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId; // 訂單 ID
    private Long userId; // 使用者 ID

    private String username; // 使用者名稱
    private String symbol; // 股票代號
    
    private int type; // 訂單類型（買入或賣出）, 0: 買入, 1: 賣出

    private Integer quantity; // 訂單數量
    private Double price; // 訂單價格

    private LocalDateTime timestamp; // 訂單建立時間
}