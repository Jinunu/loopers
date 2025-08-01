package com.loopers.domain.brand;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository {
    void save(Brand brand);

    Optional<Brand> findById(Long brandId);

    Optional<Brand> findByProductsId(Long productId);

    List<Brand> findAllByProductIds(List<Long> productIds);

}
