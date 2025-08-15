package com.loopers.domain.product;

import com.loopers.application.product.ProductPageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductQueryService {
    private final ProductRepository productRepository;

    public Page<ProductInfo> getProductInfoList(ProductPageQuery pageQuery) {
        Page<ProductInfoProjection> productInfoPage = productRepository.getProductInfoList(pageQuery.loginId(), pageQuery.brandId(), pageQuery.pageable());
        return  productInfoPage.map(proj ->
                new ProductInfo(
                        proj.getProductId(),
                        proj.getProductName(),
                        proj.getProductImageUrl(),
                        proj.getPrice(),
                        proj.getQuantity(),
                        proj.getCreatedAt(),
                        proj.getBrandId(),
                        proj.getBrandName(),
                        proj.getBrandImageUrl(),
                        proj.getLikeCount(),
                        proj.getLiked())

        );
    }
}
