package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
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
public class ProductServiceIntegrationTest {


    private static final String PRODUCT_NAME = "신발";
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final int PRODUCT_PRICE = 10000;
    private static final int PRODUCT_QUANTITY = 10;
    private static final Long PRODUCT_ID = 123L;


    @MockitoSpyBean
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("상품 조회 테스트")
    @Test
    void getProduct() {
        // arrange
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        ReflectionTestUtils.setField(product, "id", PRODUCT_ID);
        doReturn(Optional.of(product))
                .when(productRepository)
                .findById(PRODUCT_ID);
        // act
        Product result = productService.getProduct(PRODUCT_ID);
        // assert
        assertThat(result).isEqualTo(product);
    }

    @DisplayName("상품이 존재하지 않을 경우 NOT_FOUND가 반환 된다.")
    @Test
    void throwsNotFound_whenProductNotExists() {
        // act
        CoreException exception = assertThrows(CoreException.class, () -> {
            productService.getProduct(PRODUCT_ID);
        });
        // assert
        assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
    }

    @DisplayName("상품 목록 조회")
    @Test
    void getProductList() {
        // arrange
        List<Product> products = List.of(
                createProduct(1L, "나이키 신발", "nike-shoe.jpg", 89000, 5),
                createProduct(2L, "아디다스 운동화", "adidas-shoe.jpg", 79000, 3)
        );

        doReturn(products)
                .when(productRepository)
                .findAll();

        // act
        List<Product> result = productService.getProducts();

        // assert
        assertAll(
                () -> assertThat(result).hasSize(2),

                // 첫 번째 상품 검증
                () -> assertThat(result.get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getName()).isEqualTo("나이키 신발"),
                () -> assertThat(result.get(0).getImageUrl()).isEqualTo("nike-shoe.jpg"),
                () -> assertThat(result.get(0).getPrice()).isEqualTo(89000),
                () -> assertThat(result.get(0).getQuantity()).isEqualTo(5),

                // 두 번째 상품 검증
                () -> assertThat(result.get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.get(1).getName()).isEqualTo("아디다스 운동화"),
                () -> assertThat(result.get(1).getImageUrl()).isEqualTo("adidas-shoe.jpg"),
                () -> assertThat(result.get(1).getPrice()).isEqualTo(79000),
                () -> assertThat(result.get(1).getQuantity()).isEqualTo(3)
        );

    }

    private Product createProduct(Long id, String name, String imageUrl, int price, int quantity) {
        Product product = Product.of(name, imageUrl, price, quantity);
        ReflectionTestUtils.setField(product, "id", id);
        return product;
    }


}
