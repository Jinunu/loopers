package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import org.springframework.stereotype.Component;

@Component
public class ProductInfoService {
    public ProductInfo createProductInfo(Product product, Brand brand, int likeCount) {
        return ProductInfo.from(product, brand, likeCount);
    }
}
