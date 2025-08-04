package com.loopers.domain.like;

import com.loopers.domain.user.UserModel;
import lombok.Getter;

@Getter
public class LikeInfo {
    private final Long productId;
    private final int likeCount;
    private final Boolean isLiked;

    protected LikeInfo(Long productId, int likeCount, Boolean isLiked) {
        this.productId = productId;

        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }


    public static LikeInfo from(Long ProductId, int likeCount, boolean isLiked) {
        return new LikeInfo(ProductId, likeCount, isLiked);
    }
}
