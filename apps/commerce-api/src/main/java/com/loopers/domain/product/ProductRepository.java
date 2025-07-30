package com.loopers.domain.product;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {
    void save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
