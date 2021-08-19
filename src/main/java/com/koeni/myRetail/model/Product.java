package com.koeni.myRetail.model;

import java.util.Objects;

public class Product {
    private long id;
    private String name;
    private CurrentPrice current_price;

    public Product() {}

    public Product(long id, String name, CurrentPrice current_price) {
        this.id = id;
        this.name = name;
        this.current_price = current_price;
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

    public CurrentPrice getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(CurrentPrice current_price) {
        this.current_price = current_price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Objects.equals(name, product.name) && Objects.equals(current_price, product.current_price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, current_price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentPrice=" + current_price +
                '}';
    }
}
