package com.loopers.infrastructure.Brand;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {
    private final BrandJpaRepository brandJpaRepository;

    @Override
    public void save(Brand brand) {
        brandJpaRepository.save(brand);
    }

    @Override
    public Optional<Brand> findById(Long brandId) {
        return brandJpaRepository.findById(brandId);
    }

    @Override
    public Optional<Brand> findByProductsId(Long productId) {
        return brandJpaRepository.findByProductsId(productId);
    }

    @Override
    public List<Brand> findAllByProductIds(List<Long> productIds) {
        return brandJpaRepository.findAllByProductIds(productIds);
    }
}
