package com.example.market_data_service.entity;

/**
 * 市場數據實體類別，表示股票的市場數據。
 */
public class MarketData {
    private String symbol; // 股票代碼
    private double price;  // 股票價格
    private long timestamp; // 時間戳記

    /**
     * 預設建構子。
     */
    public MarketData() { }

    /**
     * 帶參數的建構子，用於初始化市場數據。
     * 
     * @param symbol 股票代碼
     * @param price 股票價格
     * @param timestamp 時間戳記
     */
    public MarketData(String symbol, double price, long timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}