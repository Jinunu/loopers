package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.Like;
import com.loopers.domain.like.LikeInfo;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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


    public List<ProductInfo> getProductInfoList(Long loginId) {

        List<Product> products = productService.getProducts();
        List<ProductInfo> productInfos = new ArrayList<>();
        for (Product product : products) {
            ProductInfo productInfo = getProductInfo(product.getId(), loginId);
            productInfos.add(productInfo);
        }
        return  productInfos.stream()
                .sorted(Sort.of(Sort.SortField.LATEST, Sort.SortDirection.DESC).getComparator())
                .toList();
    }

    public List<ProductInfo> getProductInfoList(Long loginId, Sort sort) {
        List<ProductInfo> productInfoList = getProductInfoList(loginId);
        return productInfoList.stream()
                .sorted(sort.getComparator())
                .toList();
    }
}
