package com.loopers.domain.brand;

import com.loopers.domain.product.Product;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class BrandServiceIntegrationTest {

    private static final String PRODUCT_NAME = "신발";
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final int PRODUCT_PRICE = 10000;
    private static final int PRODUCT_QUANTITY = 10;
    private static final Long PRODUCT_ID = 123L;

    private static final String BRAND_NAME = "나이킼킼";
    private static final String BRAND_IMAGE_URL = "https://example.com/brand.jpg";
    private static final Long BRAND_ID = 1L;

    @MockitoSpyBean
    private BrandRepository brandRepository;

    @Autowired
    private BrandService brandService;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("브랜드 조회 테스트")
    @Test
    void getProduct() {
        Brand brand = Brand.of(BRAND_NAME, BRAND_IMAGE_URL);
        ReflectionTestUtils.setField(brand, "id", BRAND_ID);

        doReturn(Optional.of(brand))
                .when(brandRepository)
                .findById(BRAND_ID);
        // act
        Brand result = brandService.getBrand(BRAND_ID);
        // assert
        assertThat(result).isEqualTo(brand);
    }

    @DisplayName("브랜드가 존재하지 않을 경우 NOT_FOUND가 반환 된다.")
    @Test
    void throwsNotFound_whenProductNotExists(){
        // act
        CoreException exception = assertThrows(CoreException.class, () -> {brandService.getBrand(BRAND_ID);});
        // assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }

    @DisplayName("상품ID로 브랜드정보 조회")
    @Test
    void getBrandProductByProductId() {
        Brand brand = Brand.of(BRAND_NAME, BRAND_IMAGE_URL);
        ReflectionTestUtils.setField(brand, "id", BRAND_ID);

        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        ReflectionTestUtils.setField(product, "id", PRODUCT_ID);

        brand.addProduct(product);
        doReturn(Optional.of(brand))
                .when(brandRepository)
                .findByProductsId(PRODUCT_ID);

        Brand result = brandService.getBrandByProductId(PRODUCT_ID);

        // assert
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(BRAND_ID),
                () -> assertThat(result.getName()).isEqualTo(BRAND_NAME),
                () -> assertThat(result.getImageUrl()).isEqualTo(BRAND_IMAGE_URL),
                () -> assertThat(result.getProducts()).hasSize(1),
                () -> assertThat(result.findProductById(PRODUCT_ID)).isPresent()
        );
    }

    @DisplayName("존재하지 않는 상품 ID로 브랜드 조회시 NOT_FOUND 예외가 발생한다")
    @Test
    void getBrandByProductId_ShouldThrowNotFound_WhenProductNotExists() {
        // arrange
        doReturn(Optional.empty())
                .when(brandRepository)
                .findByProductsId(PRODUCT_ID);

        // act & assert
        CoreException exception = assertThrows(CoreException.class,
                () -> brandService.getBrandByProductId(PRODUCT_ID));

        assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }
    @DisplayName("여러 상품 ID로 브랜드 정보 일괄 조회")
    @Test
    void getBrandsByProductIds() {
        // arrange
        Brand brand1 = Brand.of("나이키", "nike.jpg");
        Brand brand2 = Brand.of("아디다스", "adidas.jpg");
        ReflectionTestUtils.setField(brand1, "id", 1L);
        ReflectionTestUtils.setField(brand2, "id", 2L);

        Product product1 = Product.of("나이키 신발", "nike-shoe.jpg", 89000, 5);
        Product product2 = Product.of("아디다스 운동화", "adidas-shoe.jpg", 79000, 3);
        ReflectionTestUtils.setField(product1, "id", 1L);
        ReflectionTestUtils.setField(product2, "id", 2L);

        brand1.addProduct(product1);
        brand2.addProduct(product2);

        List<Long> productIds = List.of(1L, 2L);

        doReturn(List.of(brand1, brand2))
                .when(brandRepository)
                .findAllByProductIds(productIds);

        // act
        List<Brand> results = brandService.getBrandsByProductIds(productIds);

        // assert
        assertAll(
                () -> assertThat(results).hasSize(2),

                // 첫 번째 브랜드 검증
                () -> assertThat(results.get(0).getId()).isEqualTo(1L),
                () -> assertThat(results.get(0).getName()).isEqualTo("나이키"),
                () -> assertThat(results.get(0).getProducts()).hasSize(1),
                () -> assertThat(results.get(0).findProductById(1L)).isPresent(),

                // 두 번째 브랜드 검증
                () -> assertThat(results.get(1).getId()).isEqualTo(2L),
                () -> assertThat(results.get(1).getName()).isEqualTo("아디다스"),
                () -> assertThat(results.get(1).getProducts()).hasSize(1),
                () -> assertThat(results.get(1).findProductById(2L)).isPresent()
        );
    }




}
