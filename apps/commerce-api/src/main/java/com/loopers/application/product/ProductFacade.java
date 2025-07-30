package com.loopers.application.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductInfoService;
import com.loopers.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;
    private final ProductInfoService productInfoService;
    private final BrandService brandService;

    public ProductInfo getProductInfo(Long productId) {
        Product product = productService.getProduct(productId);
        Brand brand = brandService.getBrandByProductId(productId);
        return productInfoService.createProductInfo(product, brand, 100);
    }

}
