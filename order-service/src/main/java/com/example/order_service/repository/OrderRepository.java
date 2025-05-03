// 這個檔案是用來跟資料庫互動的，負責查詢和儲存訂單的資料。

// 這個介面是用來跟資料庫溝通的，像是查詢或儲存訂單。

package com.example.order_service.repository;

import com.example.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * 用使用者名稱查詢訂單，並按照時間從最近的排到最舊的
     * @param username 使用者名稱
     * @return 該使用者的訂單列表
     */
    // List<Order> findByUsernameOrderByTimestampDesc(String username);

    /**
     * 用使用者 ID 查詢訂單，並按照時間從最近的排到最舊的
     * @param userId 使用者的 ID
     * @return 該使用者的訂單列表
     */
    List<Order> findByUserIdOrderByTimestampDesc(Long userId);
}