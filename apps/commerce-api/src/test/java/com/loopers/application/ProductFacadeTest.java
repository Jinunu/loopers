package com.loopers.application;

import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductPageQuery;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.*;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql(scripts = "classpath:db/init-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:db/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProductFacadeTest {

    private static final String PRODUCT_NAME = "신발";
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final BigDecimal PRODUCT_PRICE = new BigDecimal("10000");
    private static final int PRODUCT_QUANTITY = 10;

    private static final String BRAND_NAME = "나이키";
    private static final String BRAND_IMAGE_URL = "https://example.com/brand.jpg";

    private static final String USER_NAME = "testuser";
    private static final String USER_EMAIL = "shwlsdn@naver.com";
    private static final String USER_BIRTH_DATE = "2001-01-01";
    private static final String GENDER = "M";


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandService brandService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ProductFacade productFacade;


    @DisplayName("상품ID로 상품 상세 정보를 조회 한다.")
    @Transactional
    @Test
    void getProductInfo_ShouldReturnProductInfo() {
        // arrange
        Brand brand = Brand.of(BRAND_NAME, BRAND_IMAGE_URL);

        Brand savedBrand = brandRepository.save(brand);
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY, savedBrand.getId());
        Product savedProduct = productRepository.save(product);

        brand.addProduct(savedProduct);

        UserModel userModel = new UserModel(USER_NAME, USER_EMAIL, USER_BIRTH_DATE, GENDER);

        UserModel savedUser = userRepository.save(userModel);

        likeService.likeProduct(savedProduct, savedUser);


        // act
        ProductInfo productInfo = productFacade.getProductInfo(savedProduct.getId() , savedUser.getId());

        // assert
        assertAll(
                // 상품 정보 검증
                () -> assertThat(productInfo.getProductId()).isEqualTo(savedProduct.getId()),
                () -> assertThat(productInfo.getProductName()).isEqualTo(savedProduct.getName()),
                () -> assertThat(productInfo.getProductImageUrl()).isEqualTo(savedProduct.getImageUrl()),
                () -> assertThat(productInfo.getPrice()).isEqualTo(savedProduct.getPrice()),
                () -> assertThat(productInfo.getQuantity()).isEqualTo(savedProduct.getQuantity()),

                // 브랜드 정보 검증
                () -> assertThat(productInfo.getBrandId()).isEqualTo(savedBrand.getId()),
                () -> assertThat(productInfo.getBrandName()).isEqualTo(savedBrand.getName()),
                () -> assertThat(productInfo.getBrandImageUrl()).isEqualTo(savedBrand.getImageUrl()),

                // 좋아요 수 검증
                () -> assertThat(productInfo.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("상품 목록 조회시 각 상품의 좋아요 수가 표시 된다. ")
    @Test
    void getProductList_ShouldReturnProductList() {
        // arrange
        UserModel userModel = userRepository.findByUserId("chulsoo");
        Product product = productRepository.findById(1L).get();
        ProductPageQuery productPageQuery = ProductPageQuery.of(userModel.getId());
        // act
        Page<ProductInfo> productInfoPage = productFacade.getProductInfoList(productPageQuery);
        List<ProductInfo> productInfos = productInfoPage.getContent();

        ProductInfo foundProductInfo = productInfos.stream()
                .filter(info -> info.getProductId().equals(product.getId()))
                .findFirst()
                .orElse(null);
        // assert
        assertAll(

                () -> assertThat(foundProductInfo).isNotNull(),
                () -> assertThat(foundProductInfo.getLikeCount()).isGreaterThanOrEqualTo(0),
                () -> assertThat(foundProductInfo.getProductName()).isNotBlank()
        );

    }

    @DisplayName("상품 목록 조회 가격 오름차순")
    @Test
    void getProductList_ShouldReturnProductListSortedByPriceAsc() {
        // arrange
        UserModel userModel = userRepository.findByUserId("chulsoo");
        // act
        ProductPageQuery productPageQuery = ProductPageQuery.of(
                userModel.getId(),Sort.by(Sort.Direction.ASC, ProductSort.SortField.PRICE.getValue()), 0, 10);

        Page<ProductInfo> productInfoPage = productFacade.getProductInfoList(productPageQuery);
        List<ProductInfo> productInfos = productInfoPage.getContent();

        // assert
        assertAll(
                () -> assertThat(productInfos).isNotEmpty(),
                () -> assertThat(productInfos.get(0).getPrice()).isLessThanOrEqualTo(productInfos.get(1).getPrice())
        );
    }

    @DisplayName("상품 목록 조회 기본 정렬 조건 상품 최신순")
    @Test
    void getProductList_ShouldReturnProductListSortedByCreatedAtDesc() {
        // arrange
        UserModel userModel = userRepository.findByUserId("chulsoo");
        // act
        ProductPageQuery productPageQuery = ProductPageQuery.of(userModel.getId());
        Page<ProductInfo> productInfoPage = productFacade.getProductInfoList(productPageQuery);
        List<ProductInfo> productInfos = productInfoPage.getContent();

        // assert
        assertAll(
                () -> assertThat(productInfos).isNotEmpty(),
                () -> assertThat(productInfos.get(0).getCreatedAt()).isAfterOrEqualTo(productInfos.get(1).getCreatedAt()),
                () -> assertThat(productInfos.get(0).getCreatedAt()).isAfterOrEqualTo(productInfos.get(2).getCreatedAt()),
                () -> assertThat(productInfos.get(1).getCreatedAt()).isAfterOrEqualTo(productInfos.get(2).getCreatedAt())
        );
    }

    @DisplayName("상품 목록 조회 가격 내림차순")
    @Test
    void getProductList_ShouldReturnProductListSortedByPriceDesc() {
        // arrange
        UserModel userModel = userRepository.findByUserId("chulsoo");
        ProductPageQuery productPageQuery = ProductPageQuery.of(userModel.getId(), Sort.by(Sort.Direction.DESC, ProductSort.SortField.PRICE.getValue()), 0, 10);
        // act

        Page<ProductInfo> productInfoPage = productFacade.getProductInfoList(productPageQuery);
        List<ProductInfo> productInfos = productInfoPage.getContent();

        // assert
        assertAll(
                () -> assertThat(productInfos).isNotEmpty(),
                () -> assertThat(productInfos.get(0).getPrice()).isGreaterThanOrEqualTo(productInfos.get(1).getPrice()),
                () -> assertThat(productInfos.get(1).getPrice()).isGreaterThanOrEqualTo(productInfos.get(2).getPrice())
        );
    }
}
