package com.loopers.domain.like;

import com.loopers.domain.product.Product;
import com.loopers.domain.user.UserModel;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public void likeProduct(Product product, UserModel userModel) {
        Like like = Like.likeProduct(product, userModel);
        likeRepository.findByProductIdAndUserId(product.getId(), userModel.getId())
                .ifPresent(existingLike -> {
                    throw new CoreException(ErrorType.BAD_REQUEST, "이미 좋아요를 누른 상품입니다.");
                });
        likeRepository.save(like);
    }

    public List<Like> getLikeByProductId(Long productId) {
        return likeRepository.findByProductId(productId);
    }

    public int countLike(Long productId) {
        return likeRepository.countByProductId(productId);
    }

    public boolean hasLiked(Long productId, Long loginId) {
        return likeRepository.findByProductIdAndUserId(productId, loginId).isPresent();
    }

    public Like findLikeByProductIdAndUserId(Long productId, Long id) {
        return likeRepository.findByProductIdAndUserId(productId, id).orElse(null);
    }
}
