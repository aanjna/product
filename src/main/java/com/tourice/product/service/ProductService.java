package com.tourice.product.service;

import com.tourice.product.model.Product;
import com.tourice.product.model.ProductDto;
import com.tourice.product.model.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDto productDto);

    void deleteProduct(Long id);

    ProductResponse findAllProducts(int pageNo, int pageSize);

    List<Product> getRecentViewedProducts(int limit);

    Product getProduct(Long id, String currency);

    List<Product> getProductsFilterByValue(String filter, String value);

    Page<Product> search(int pageNumber, int size, String searchKey);
}
