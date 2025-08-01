package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {
    private static final String PRODUCT_NAME = "신발";
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final int PRODUCT_PRICE = 10000;
    private static final int PRODUCT_QUANTITY = 10;
    private static final Long PRODUCT_ID = 123L;

    @DisplayName("상품 생성")
    @Test
    void createProduct() {
        String name = "신발";
        String imageUrl = "https://example.com/image.jpg";
        int price = 10000;
        int quantity = 10;

        Product product = Product.of(name, imageUrl, price, quantity);
        Assertions.assertAll(() -> assertThat(product.getName()).isEqualTo(name), () -> assertThat(product.getImageUrl()).isEqualTo(imageUrl), () -> assertThat(product.getPrice()).isEqualTo(price), () -> assertThat(product.getQuantity()).isEqualTo(quantity));

    }

    @DisplayName("차감할 재고의 수가 상품의 남은 재고의 수 보다 작은 경우 정상 차감 된다.")
    @Test
    void product_Decrease_Quantity() {
        // arrange
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        ReflectionTestUtils.setField(product, "id", PRODUCT_ID);
        int minusQuantity = 10;
        int currentQuantity = product.getQuantity();
        // act
        product.decreaseQuantity(minusQuantity);

        // arrange

        assertThat(product.getQuantity()).isEqualTo(currentQuantity - minusQuantity);
    }



    @DisplayName("차감할 재고의 수가 음수인 경우 BAD_REQUEST 예외가 발생된다")
    @Test
    void throwsBadRequestException_whenMinusQuantityIsNegative() {
        // arrange
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        // act and assert
        assertThatThrownBy(() -> product.decreaseQuantity(-3)).isInstanceOf(CoreException.class).hasFieldOrPropertyWithValue("errorType", ErrorType.BAD_REQUEST);

    }

    @DisplayName("차감할 재고의 수가 남은 재고의 수 보다 큰 경우 BAD_REQUEST 예외가 발생된다")
    @Test
    void throwsBadRequestException_whenMinusQuantityIsGreaterThanAvailableQuantity() {
        // arange
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        int currentQuantity = product.getQuantity();

        // act and assert
        assertThatThrownBy(() -> product.decreaseQuantity(currentQuantity + 1)).isInstanceOf(CoreException.class).hasFieldOrPropertyWithValue("errorType", ErrorType.BAD_REQUEST);

    }


}
