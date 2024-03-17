package com.tourice.product.service;

import com.tourice.product.model.Product;
import com.tourice.product.model.ProductDto;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDto productDto);

    Product getProductById(Long id);

    void deleteProduct(Long id);

    List<Product> findAllProducts();

    List<Product> getRecentViewedProducts(int limit);
}
