package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductInfoService {
    public ProductInfo createProductInfo(Product product, Brand brand, LikeInfo likeInfo) {
        return ProductInfo.from(product, brand, likeInfo);
    }
}
