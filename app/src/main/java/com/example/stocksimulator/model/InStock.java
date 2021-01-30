package com.example.stocksimulator.model;

public class InStock {

    private String stockId;
    private String stockName;
    private Float price;
    private Integer amount;

    public InStock(String stockId, String stockName, Float price, Integer amount) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.price = price;
        this.amount = amount;
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

}
