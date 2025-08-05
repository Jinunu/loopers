package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.LikeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class ProductInfo {
    private final Long productId;
    private final String productName;
    private final String productImageUrl;
    private final BigDecimal price;
    private final int quantity;
    private final ZonedDateTime createdAt;

    private final Long brandId;
    private final String brandName;
    private final String brandImageUrl;

    private final int likeCount;

    private final boolean isLiked;

    public static ProductInfo from(Product product, Brand brand, LikeInfo likeInfo) {
        return new ProductInfo(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                product.getQuantity(),
                product.getCreatedAt(),
                brand.getId(),
                brand.getName(),
                brand.getImageUrl(),
                likeInfo.getLikeCount(),
                likeInfo.getIsLiked()
        );
    }

}
