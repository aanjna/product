package com.tourice.product.service;

import com.tourice.product.model.*;
import com.tourice.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private ModelMapper modelMapper;
    private RestTemplate restTemplate;
    private final String currencyUrl = "https://currencylayer.com/";

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public Product createProduct(ProductDto productDto) {
        Product product = new Product();
        if (productDto != null) {
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());
            product.setCurrency(Currency.valueOf(productDto.getCurrency()));
            product.setViewCount(0);
        }
        return productRepository.save(product);
    }

    public Product getProduct(Long id, String currency) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            addProductView(id);
        }

        // Convert price if necessary
        if (!currency.equals("USD")) {
            double convertedPrice = convertCurrency(product.getPrice(), "USD", currency);
            product.setPrice(convertedPrice);
        }

        return product;
    }

    @Override
    public List<Product> getProductsFilterByValue(String groupByValue, String actualvalue) {
        GroupBy groupBy = GroupBy.valueOf(groupByValue.toUpperCase());
        switch (groupBy) {
            case BRAND:
                return productRepository.findByBrandId(Integer.valueOf(actualvalue));
            case CATEGORY:
                return productRepository.findByCategoryId(Integer.valueOf(actualvalue));
            case NAME:
                return productRepository.findByName(actualvalue);
            default:
                return null;
        }
    }

    @Override
    public Page<Product> search(int pageNumber, int size, String searchKey) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Product> productPage = productRepository.search(searchKey, request);
        //return new Page<>(productPage, Page.of(productPage.getTotalPages(), pageNumber, size));
        return null;
    }

    private void addProductView(Long id) {
        productRepository.incrementViewCount(id);
    }

    public void deleteProduct(Long id) {
        //cans et status active as false here for soft delete
        Product product = productRepository.findById(id).orElse(null);
        product.setActive(Boolean.FALSE);
        productRepository.save(product);
//        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse findAllProducts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Product> productList = productRepository.findAll(pageable);
        List<Product> listOfPost = productList.getContent();
        List<ProductDto> content = listOfPost.stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());

        ProductResponse postResponse = new ProductResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(productList.getNumber());
        postResponse.setPageSize(productList.getSize());
        postResponse.setTotalElements(productList.getTotalElements());
        postResponse.setTotalPages(productList.getTotalPages());
        postResponse.setLast(productList.isLast());
        return postResponse;
    }

    @Override
    public List<Product> getRecentViewedProducts(int limit) {
        return null;
    }

    // method for currency conversion
    private double convertCurrency(double amount, String baseCurrency, String targetCurrency) {
        String conversionApiUrl = currencyUrl + baseCurrency;
        CurrencyResponse currencyResponse = restTemplate.getForObject(conversionApiUrl, CurrencyResponse.class);

        // Get the appropriate rate based on targetCurrency
        double conversionRate;
        switch (targetCurrency) {
            case "USD":
                conversionRate = currencyResponse.getRates().getUSD();
                break;
            case "CAD":
                conversionRate = currencyResponse.getRates().getCAD();
                break;
            case "EUR":
                conversionRate = currencyResponse.getRates().getEUR();
                break;
            case "GBP":
                conversionRate = currencyResponse.getRates().getGBP();
                break;
            default:
                throw new IllegalArgumentException("Invalid target currency");
        }

        return amount * conversionRate;
    }

}
