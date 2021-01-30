package com.example.stocksimulator.model;

public class TradeDetail {

    private String tradeId;
    private String stockName;
    private String stockId;
    private Integer amount;
    private Float price;
    private String date;
    private String tradeStatus;

    public TradeDetail(String tradeId, String stockName, String stockId, Integer amount, Float price, String date, String tradeStatus) {
        this.tradeId = tradeId;
        this.stockName = stockName;
        this.stockId = stockId;
        this.amount = amount;
        this.price = price;
        this.date = date;
        this.tradeStatus = tradeStatus;
    }

    public String getTradeId() {
        return tradeId;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public String getStockName() {
        return stockName;
    }

    public String getStockId() {
        return stockId;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

}
