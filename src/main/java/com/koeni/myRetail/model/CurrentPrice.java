package com.koeni.myRetail.model;

import java.util.Objects;

public class CurrentPrice {

    double value;
    String currency_code;

    public CurrentPrice() {
    }

    public CurrentPrice(double value, String currency_code) {
        this.value = value;
        this.currency_code = currency_code;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentPrice that = (CurrentPrice) o;
        return Double.compare(that.value, value) == 0 && Objects.equals(currency_code, that.currency_code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency_code);
    }

    @Override
    public String toString() {
        return "CurrentPrice{" +
                "value=" + value +
                ", currencyCode='" + currency_code + '\'' +
                '}';
    }
}
