package com.loopers.domain.like;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository {
    void save(Like like);

    List<Like> findByProductId(Long productId);


    Optional<Object> findByProductIdAndUserId(Long productId, Long userId);
}
