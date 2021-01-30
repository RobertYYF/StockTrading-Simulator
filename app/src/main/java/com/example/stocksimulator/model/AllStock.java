package com.example.stocksimulator.model;

import java.util.Map;

public class AllStock {

    private Map<String, Stock> allStocks;

    public AllStock(Map<String, Stock> allStocks) {
        this.allStocks = allStocks;
    }

    public Map<String, Stock> getAllStocks() {
        return allStocks;
    }

    public void setAllStocks(Map<String, Stock> allStocks) {
        this.allStocks = allStocks;
    }
}