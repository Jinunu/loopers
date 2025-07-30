package com.loopers.infrastructure.Brand;


import com.loopers.domain.brand.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BrandJpaRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByProductsId(Long productId);
    @Query("SELECT DISTINCT b FROM Brand b JOIN FETCH b.products p WHERE p.id IN :productIds")
    List<Brand> findAllByProductIds(@Param("productIds") List<Long> productIds);

}
