package com.tourice.product.repository;

import com.tourice.product.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    void incrementViewCount(@Param("id") Long id);

    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.brand WHERE p.category = :categoryId")
    List<Product> findByCategoryId(@Param(value = "colorId") Integer categoryId);

    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.brand WHERE p.brand = :brandId")
    List<Product> findByBrandId(@Param(value = "brandId") Integer brandId);

    List<Product> findBySize(String actualvalue);
}
