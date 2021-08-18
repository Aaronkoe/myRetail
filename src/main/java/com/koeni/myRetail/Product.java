package com.koeni.myRetail;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Product {
    private @Id long id;
    private String name;
    private double currentPrice;
    private String currencyCode;

    public Map<String, Object> toRequestStructure() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", this.id);
        response.put("name", this.name);
        Map<String, Object> currentPrice = new HashMap<>();
        currentPrice.put("value", this.currentPrice);
        currentPrice.put("currency_code", this.currencyCode);
        response.put("current_price", currentPrice);
        return response;
    }

    public static Product fromRequestStructure(Map<String, Object> request) {
        Map<String, Object> currentPrice = (Map<String, Object>)request.get("current_price");
        double value = (double)currentPrice.get("value");
        String currencyCode = (String)currentPrice.get("currency_code");
        Integer id = (Integer)request.get("id");
        String name = (String)request.get("name");
        return new Product(id, name, value, currencyCode);
    }

    public Product() {}

    public Product(long id, String name, double currentPrice, String currencyCode) {
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
        this.currencyCode = currencyCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
