package com.tourice.product.model;

public enum GroupBy {
    BRAND("brand"), COLOR("color"), SIZE("size");
    String value;

    GroupBy(String name) {
        value = name;
    }

    String getValue() {
        return value;
    }
}
