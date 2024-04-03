package com.tourice.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Brand {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String brandName;

    @JsonIgnoreProperties("brand")
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    private Set<Product> products;

}
