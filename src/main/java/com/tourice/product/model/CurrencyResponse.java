package com.tourice.product.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyResponse {
    private String base;
    private String date;
    private Rates rates;
}
