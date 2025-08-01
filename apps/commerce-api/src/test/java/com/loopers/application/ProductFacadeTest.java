package com.loopers.application;

import com.loopers.application.product.ProductFacade;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class ProductFacadeTest {

    private static final String PRODUCT_NAME = "신발";
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final int PRODUCT_PRICE = 10000;
    private static final int PRODUCT_QUANTITY = 10;
    private static final Long PRODUCT_ID = 123L;

    private static final String BRAND_NAME = "나이키";
    private static final String BRAND_IMAGE_URL = "https://example.com/brand.jpg";
    private static final Long BRAND_ID = 1L;

    private static final int LIKE_COUNT = 100;

    @MockitoSpyBean
    private ProductService productService;

    @MockitoSpyBean
    private BrandService brandService;

    @Autowired
    private ProductFacade productFacade;


    @DisplayName("상품ID로 상품 상세 정보를 조회 한다.")
    @Test
    void getProductInfo_ShouldReturnProductInfo() {
        // arrange
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        ReflectionTestUtils.setField(product, "id", PRODUCT_ID);

        Brand brand = Brand.of(BRAND_NAME, BRAND_IMAGE_URL);
        ReflectionTestUtils.setField(brand, "id", BRAND_ID);

        doReturn(product)
                .when(productService)
                .getProduct(PRODUCT_ID);

        doReturn(brand)
                .when(brandService)
                .getBrandByProductId(PRODUCT_ID);

        // act
        ProductInfo productInfo = productFacade.getProductInfo(PRODUCT_ID);

        // assert
        assertAll(
                // 상품 정보 검증
                () -> assertThat(productInfo.getProductId()).isEqualTo(PRODUCT_ID),
                () -> assertThat(productInfo.getProductName()).isEqualTo(PRODUCT_NAME),
                () -> assertThat(productInfo.getProductImageUrl()).isEqualTo(PRODUCT_IMAGE_URL),
                () -> assertThat(productInfo.getPrice()).isEqualTo(PRODUCT_PRICE),
                () -> assertThat(productInfo.getQuantity()).isEqualTo(PRODUCT_QUANTITY),

                // 브랜드 정보 검증
                () -> assertThat(productInfo.getBrandId()).isEqualTo(BRAND_ID),
                () -> assertThat(productInfo.getBrandName()).isEqualTo(BRAND_NAME),
                () -> assertThat(productInfo.getBrandImageUrl()).isEqualTo(BRAND_IMAGE_URL),

                // 좋아요 수 검증
                () -> assertThat(productInfo.getLikeCount()).isEqualTo(LIKE_COUNT)
        );
    }



}
