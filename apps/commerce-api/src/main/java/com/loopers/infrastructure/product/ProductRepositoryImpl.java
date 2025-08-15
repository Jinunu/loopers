package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductInfoProjection;
import com.loopers.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }

    @Override
    public Optional<Product> findByName(String productName) {
        return productJpaRepository.findByName(productName);
    }

    @Override
    public Optional<Product> findByIdWithPessimisticLock(Long productId) {
        return productJpaRepository.findByIdWithPessimisticLock(productId);
    }

    @Override
    public Page<ProductInfoProjection> getProductInfoList(Long loginId, Long brandId, Pageable pageable) {
        return productJpaRepository.getProductInfoList(loginId, brandId, pageable);
    }

}
