package com.tourice.product.model;

public enum GroupBy {
    BRAND("brand"), CATEGORY("category"), NAME("name");
    String value;

    GroupBy(String name) {
        value = name;
    }

    String getValue() {
        return value;
    }
}
