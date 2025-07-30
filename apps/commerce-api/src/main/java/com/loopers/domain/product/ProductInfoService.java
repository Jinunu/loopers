package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;

public class ProductInfoService {
    public static ProductInfo createProductInfo(Product product, Brand brand, int likeCount) {
        return ProductInfo.from(product, brand, likeCount);
    }
}
