package com.loopers.application;

import com.loopers.application.product.ProductFacade;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import com.loopers.domain.user.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class ProductFacadeTest {

    private static final String PRODUCT_NAME = "신발";
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final int PRODUCT_PRICE = 10000;
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

    @DisplayName("상품 목록 조회")
    @Test
    void getProductList_ShouldReturnProductList() {
        // arrange
        Brand brand = Brand.of(BRAND_NAME, BRAND_IMAGE_URL);

        Brand savedBrand = brandRepository.save(brand);

        Product product1 = Product.of("나이키 신발", "nike-shoe.jpg", 89000, 5, savedBrand.getId());
        Product product2 = Product.of("아디다스 운동화", "adidas-shoe.jpg", 79000, 3, savedBrand.getId());
        Product savedProduct1 = productRepository.save(product1);
        Product savedProduct2 = productRepository.save(product2);

        brand.addProduct(savedProduct1);
        brand.addProduct(savedProduct2);

        UserModel userModel = new UserModel(USER_NAME, USER_EMAIL, USER_BIRTH_DATE, GENDER);

        UserModel savedUser = userRepository.save(userModel);

        likeService.likeProduct(savedProduct1, savedUser);
        likeService.likeProduct(savedProduct2, savedUser);

        // act
        List<ProductInfo> productInfos = productFacade.getProductInfoList(savedUser.getId());

        // assert
        assertAll(
                () -> assertThat(productInfos).hasSize(2),
                () -> assertThat(productInfos.get(0).getProductName()).isEqualTo("나이키 신발"),
                () -> assertThat(productInfos.get(1).getProductName()).isEqualTo("아디다스 운동화"),
                () -> assertThat(productInfos.get(0).getBrandName()).isEqualTo(BRAND_NAME),
                () -> assertThat(productInfos.get(1).getBrandName()).isEqualTo(BRAND_NAME),
                () -> assertThat(productInfos.get(0).getLikeCount()).isEqualTo(1),
                () -> assertThat(productInfos.get(1).getLikeCount()).isEqualTo(1),
                () -> assertThat(productInfos.get(0).getProductId()).isEqualTo(savedProduct1.getId()),
                () -> assertThat(productInfos.get(1).getProductId()).isEqualTo(savedProduct2.getId()),
                () -> assertThat(productInfos.get(0).isLiked()).isTrue(),
                () -> assertThat(productInfos.get(1).isLiked()).isTrue()


        );
    }
}
