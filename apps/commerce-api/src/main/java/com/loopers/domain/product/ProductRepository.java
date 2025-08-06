package com.loopers.domain.product;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    Optional<Product> findByName(String productName);

    List<Product> findProductsByIds(List<Long> productIds);
}
