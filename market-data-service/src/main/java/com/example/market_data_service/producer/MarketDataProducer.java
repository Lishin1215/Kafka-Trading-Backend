package com.example.market_data_service.producer;

import com.example.market_data_service.entity.MarketData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

/**
 * Kafka 生產者服務，負責定期生成市場數據並發送到 Kafka 主題。
 */
@Service
public class MarketDataProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MarketDataProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 當應用程式啟動後，開始定期生成市場數據並發送到 Kafka 主題。
     */
    @EventListener(ApplicationReadyEvent.class)
    public void startGenerating() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            // 創建市場數據物件
            MarketData data1 = new MarketData("AAPL", 100 + Math.random() * 10, System.currentTimeMillis());
            MarketData data2 = new MarketData("TSLA", 100 + Math.random() * 10, System.currentTimeMillis());
            try {
                // 將市場數據轉換為 JSON 並發送到 Kafka
                String json1 = objectMapper.writeValueAsString(data1);
                String json2 = objectMapper.writeValueAsString(data2);
                kafkaTemplate.send("market-data", json1);
                kafkaTemplate.send("market-data", json2);
                System.out.println("Produced: " + json1);
                System.out.println("Produced: " + json2);
            } catch (Exception e) {
                // 處理可能的例外
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}