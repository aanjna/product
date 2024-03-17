package com.tourice.product.model;

public enum Currency {
    USD("US Dollar"),
    CAD("Canadian Dollar"),
    EUR("Euro"),
    GBP("British Pound");

    private final String description;

    Currency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
