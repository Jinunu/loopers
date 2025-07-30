package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductInfo {
    private final Long productId;
    private final String productName;
    private final String productImageUrl;
    private final int price;
    private final int quantity;

    private final Long brandId;
    private final String brandName;
    private final String brandImageUrl;

    private final int likeCount;

    public static ProductInfo from(Product product, Brand brand, int likeCount) {
        return new ProductInfo(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                product.getQuantity(),
                brand.getId(),
                brand.getName(),
                brand.getImageUrl(),
                likeCount
        );
    }

}
