package com.example.stocksimulator.model;

import java.io.Serializable;

public class Stock {

    private String code;
    private String name;
    private Float price;
    private String percent;

    public Stock(String code, String name, Float price, String percent) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.percent = percent;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercent() {
        return percent;
    }

}