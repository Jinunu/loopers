package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeInfo;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;
    private final ProductInfoService productInfoService;
    private final BrandService brandService;
    private final LikeService likeService;
    private final ProductQueryService productQueryService;

    public ProductInfo getProductInfo(Long productId, Long loginId) {
        Product product = productService.getProduct(productId);
        Brand brand = brandService.getBrandByProductId(productId);
        int likeCount = product.getLikeCount();
        boolean hasLiked = likeService.hasLiked(productId, loginId);
        LikeInfo likeInfo = LikeInfo.from(productId, likeCount, hasLiked);
        return productInfoService.createProductInfo(product, brand,  likeInfo);
    }


    public Page<ProductInfo> getProductInfoList(ProductPageQuery pageQuery) {
        return productQueryService.getProductInfoList(pageQuery);
    }
}
