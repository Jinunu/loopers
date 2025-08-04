package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeInfo;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductInfoService;
import com.loopers.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;
    private final ProductInfoService productInfoService;
    private final BrandService brandService;
    private final LikeService likeService;

    public ProductInfo getProductInfo(Long productId, Long loginId) {
        Product product = productService.getProduct(productId);
        Brand brand = brandService.getBrandByProductId(productId);
        int likeCount = likeService.countLike(productId);
        boolean hasLiked = likeService.hasLiked(productId, loginId);
        LikeInfo likeInfo = LikeInfo.from(productId, likeCount, hasLiked);
        return productInfoService.createProductInfo(product, brand,  likeInfo);
    }

}
