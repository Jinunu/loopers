package com.loopers.interfaces.api.product;

import com.loopers.domain.product.ProductInfo;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class ProductV1Dto {

    public record ProductDetailResponse(
            Long productId,
            String productName,
            String productImageUrl,
            BigDecimal price,
            int quantity,
            ZonedDateTime createdAt,
            Long brandId,
            String brandName,
            String brandImageUrl,
            int likeCount,
            boolean liked
    ) {
        public static ProductDetailResponse from(ProductInfo info) {
            return new ProductDetailResponse(
                    info.getProductId(),
                    info.getProductName(),
                    info.getProductImageUrl(),
                    info.getPrice(),
                    info.getQuantity(),
                    info.getCreatedAt(),
                    info.getBrandId(),
                    info.getBrandName(),
                    info.getBrandImageUrl(),
                    info.getLikeCount(),
                    info.isLiked()
            );
        }
    }

    public record ProductListItem(
            Long productId,
            String productName,
            String productImageUrl,
            BigDecimal price,
            int quantity,
            ZonedDateTime createdAt,
            Long brandId,
            String brandName,
            String brandImageUrl,
            int likeCount,
            boolean liked
    ) {
        public static ProductListItem from(ProductInfo info) {
            return new ProductListItem(
                    info.getProductId(),
                    info.getProductName(),
                    info.getProductImageUrl(),
                    info.getPrice(),
                    info.getQuantity(),
                    info.getCreatedAt(),
                    info.getBrandId(),
                    info.getBrandName(),
                    info.getBrandImageUrl(),
                    info.getLikeCount(),
                    info.isLiked()
            );
        }
    }
}
