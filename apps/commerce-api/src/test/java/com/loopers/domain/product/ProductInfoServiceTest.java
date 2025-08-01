package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductInfoServiceTest {
    private static final String PRODUCT_NAME = "신발";
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final int PRODUCT_PRICE = 10000;
    private static final int PRODUCT_QUANTITY = 10;
    private static final Long PRODUCT_ID = 123L;

    private static final String BRAND_NAME = "나이킼킼";
    private static final String BRAND_IMAGE_URL = "https://example.com/brand.jpg";
    private static final Long BRAND_ID = 1L;

    private static final int LIKE_COUNT = 500;

    @DisplayName("상품, 브랜드, 좋아요 수로 상품 정보를 생성한다.")
    @Test
    void createProductInfoTest(){
        // arrange
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        ReflectionTestUtils.setField(product, "id", PRODUCT_ID);


        Brand brand = Brand.of(BRAND_NAME, BRAND_IMAGE_URL);
        ReflectionTestUtils.setField(brand, "id", BRAND_ID);

        // act
        ProductInfo productInfo = new ProductInfoService().createProductInfo(product, brand, LIKE_COUNT);

        // assert
        assertAll(
                // 상품 정보 검증
                () -> assertThat(productInfo.getProductId()).isEqualTo(product.getId()),
                () -> assertThat(productInfo.getProductName()).isEqualTo(product.getName()),
                () -> assertThat(productInfo.getProductImageUrl()).isEqualTo(product.getImageUrl()),
                () -> assertThat(productInfo.getPrice()).isEqualTo(product.getPrice()),
                () -> assertThat(productInfo.getQuantity()).isEqualTo(product.getQuantity()),

                // 브랜드 정보 검증
                () -> assertThat(productInfo.getBrandId()).isEqualTo(brand.getId()),
                () -> assertThat(productInfo.getBrandName()).isEqualTo(brand.getName()),
                () -> assertThat(productInfo.getBrandImageUrl()).isEqualTo(brand.getImageUrl()),

                // 좋아요 수 검증
                () -> assertThat(productInfo.getLikeCount()).isEqualTo(LIKE_COUNT)


        );

    }
}
