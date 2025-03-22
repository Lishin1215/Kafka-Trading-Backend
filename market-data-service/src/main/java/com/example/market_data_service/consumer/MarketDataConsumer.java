package com.example.market_data_service.consumer;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka 消費者服務，負責從 Kafka 主題接收市場數據並透過 WebSocket 傳送給客戶端。
 */
@Service
public class MarketDataConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public MarketDataConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 消費 Kafka 主題中的市場數據，並透過 WebSocket 傳送給客戶端。
     * 
     * @param message 接收到的市場數據訊息
     */
    @KafkaListener(topics = "market-data", groupId = "market-data-group")
    public void consume(String message) {
        System.out.println("Consumed: " + message);
        messagingTemplate.convertAndSend("/topic/market-data", message);
    }
}
