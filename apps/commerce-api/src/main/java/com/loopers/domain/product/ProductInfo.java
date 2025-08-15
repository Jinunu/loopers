package com.loopers.domain.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.LikeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
public class ProductInfo implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @JsonCreator  // Jackson이 이 생성자를 사용하도록 지시
    public ProductInfo(
            @JsonProperty("productId") Long productId,
            @JsonProperty("productName") String productName,
            @JsonProperty("productImageUrl") String productImageUrl,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("createdAt") ZonedDateTime createdAt,
            @JsonProperty("brandId") Long brandId,
            @JsonProperty("brandName") String brandName,
            @JsonProperty("brandImageUrl") String brandImageUrl,
            @JsonProperty("likeCount") int likeCount,
            @JsonProperty("isLiked") boolean isLiked) {
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandImageUrl = brandImageUrl;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

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

    @Override
    public String toString() {
        return "ProductInfo{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", brandName='" + brandName + '\'' +
                ", likeCount=" + likeCount +
                ", isLiked=" + isLiked +
                '}';
    }
}
