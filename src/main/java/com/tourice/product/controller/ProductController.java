package com.tourice.product.controller;

import com.tourice.product.model.Product;
import com.tourice.product.model.ProductDto;
import com.tourice.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> addNewProduct(@RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
        return new ResponseEntity<>("Product Created successfully", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        return new ResponseEntity<>(productService.findAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id, @RequestParam(required = false, defaultValue = "USD") String currency) {
        Product product = productService.getProduct(id, currency);
        if (product != null)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Product require Soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/most-viewed")
    public ResponseEntity<List<Product>> getMostViewedProducts(int limit) {
        // Use JPA queries or stored procedures to retrieve recent views
        return new ResponseEntity<>(productService.getRecentViewedProducts(limit), HttpStatus.OK);
    }

    @GetMapping("/filter/{filter}/{value}")
    public ResponseEntity<List<Product>> getProducts(@PathVariable String filter,
                                                     @PathVariable String value) {
        List<Product> products = productService.getProductsFilterByValue(filter, value);
        if(products==null || products.isEmpty())
            throw new RuntimeException("Product not found!");

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
