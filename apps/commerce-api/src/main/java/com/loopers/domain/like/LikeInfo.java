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

    public static LikeInfo from(Like like, int likeCount, UserModel loginUser) {
        if (like == null) {
            return new LikeInfo(null, 0, false);
        }

        boolean isLiked = false;
        if (loginUser != null) {
            isLiked = like.isLiked(loginUser);
        }

        return new LikeInfo(like.getProduct().getId(), likeCount, isLiked);
    }
}
