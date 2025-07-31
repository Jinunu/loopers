package com.loopers.infrastructure.like;

import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
    private final LikeJpaRepository likeJpaRepository;


    @Override
    public void save(Like like) {
        likeJpaRepository.save(like);
    }

    @Override
    public List<Like> findByProductId(Long productId) {
        return likeJpaRepository.findByProductId(productId);
    }

    @Override
    public Optional<Object> findByProductIdAndUserId(Long productId, Long userId) {
        return likeJpaRepository.findByProductIdAndUserId(productId, userId);
    }
}
