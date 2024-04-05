package com.tourice.product.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private String name;
    private double price;
    private String description;
    private String currency;
    private int viewCount;
    private boolean isActive;
    private String category;
}
