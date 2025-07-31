package com.loopers.infrastructure.like;

import com.loopers.domain.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {
    @Query("SELECT l FROM Like l WHERE l.product.id = ?1")
    List<Like> findByProductId(Long productId);

    @Query("SELECT l FROM Like l WHERE l.product.id = ?1 AND l.userModel.id = ?2")
    Optional<Object> findByProductIdAndUserId(Long productId, Long userId);
}
