package com.koeni.myRetail;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Product {
    private @Id long id;
    private String name;
    private float currentPrice;
    private String currencyCode;

    public Product() {}

    public Product(long id, String name, float currentPrice, String currencyCode) {
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

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
