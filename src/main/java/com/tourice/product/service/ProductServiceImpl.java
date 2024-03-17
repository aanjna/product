package com.tourice.product.service;

import com.tourice.product.model.Currency;
import com.tourice.product.model.Product;
import com.tourice.product.model.ProductDto;
import com.tourice.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductDto productDto) {
        Product product = new Product();
        if (productDto != null) {
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            product.setCurrency(Currency.valueOf(productDto.getCurrency()));
        }
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if(product!=null){
            boolean viewedRecently = checkRecentView(id);
            if (!viewedRecently) {
                addProductView(id);
            }
        }
        return product;
    }

    private void addProductView(Long id) {
        // Insert new entry into product_view table
    }

    private boolean checkRecentView(Long id) {
//        logic to check for recent view
        return true;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getRecentViewedProducts(int limit) {
        return null;
    }
}
