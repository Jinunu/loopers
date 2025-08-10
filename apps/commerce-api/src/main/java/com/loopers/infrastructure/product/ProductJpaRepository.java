package com.loopers.infrastructure.product;


import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductInfoProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String productName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :productId")
    Optional<Product> findByIdWithPessimisticLock(Long productId);

    @Query(value = """
            
            SELECT
                    p.id as productId                                                                                              
                  , p.name                                                    as   productName 
                  , p.imageUrl                                                as productImageUrl
                  , p.price                                                   as price
                  , p.quantity                                                as quantity
                  , p.createdAt                                               as createdAt
                  , b.id                                                      as brandId
                  , b.name                                                    as brandName
                  , b.imageUrl                                                as brandImageUrl
                  , (select count(l.id) from Like l where l.product.id = p.id) as likeCount
                  , (case
                        when (select count(lk2.id)
                              from Like lk2
                              where lk2.product.id = p.id
                              and lk2.userModel.id = :loginId) > 0
                        then true
                        else false end)  as liked
            FROM Product p
            LEFT JOIN Brand b ON p.brandId = b.id
            """,
            countQuery = """
                    select count(p.id)
                    from Product p
                    """
    )
    Page<ProductInfoProjection> getProductInfoList(Long loginId, Pageable pageable);
}
