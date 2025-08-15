package com.loopers.domain.like;

import com.loopers.domain.product.Product;
import com.loopers.domain.user.UserModel;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    @Transactional
    public void likeProduct(Product product, UserModel userModel) {
        likeRepository.findByProductIdAndUserId(product.getId(), userModel.getId())
                .ifPresent(existingLike -> {
                    throw new CoreException(ErrorType.BAD_REQUEST, "이미 좋아요를 누른 상품입니다.");
                });
        Like like = Like.likeProduct(product, userModel);
        likeRepository.save(like);
        product.increaseLikeCount();
    }

    @Transactional
    public void unlikeProduct(Product product, UserModel userModel) {
        Like like = likeRepository.findByProductIdAndUserId(product.getId(), userModel.getId())
                .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "좋아요가 존재하지 않습니다."));
        likeRepository.delete(like);
        product.decreaseLikeCount();
    }


    public int countLike(Long productId) {
        return likeRepository.countByProductId(productId);
    }

    public boolean hasLiked(Long productId, Long loginId) {
        return likeRepository.findByProductIdAndUserId(productId, loginId).isPresent();
    }

}
