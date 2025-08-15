package com.loopers.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    Optional<Product> findByName(String productName);


    Optional<Product> findByIdWithPessimisticLock(Long productId);

    Page<ProductInfoProjection> getProductInfoList(Long loginId, Long brandId, Pageable pageable);
}
