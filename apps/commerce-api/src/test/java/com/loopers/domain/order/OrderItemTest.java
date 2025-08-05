package com.loopers.domain.order;

import com.loopers.domain.product.Product;
import com.loopers.domain.user.UserModel;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class OrderItemTest {
    private static final String PRODUCT_NAME = "신발";
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final BigDecimal PRODUCT_PRICE = new BigDecimal("10000") ;
    private static final int PRODUCT_QUANTITY = 10;
    private static final Long PRODUCT_ID = 123L;
    private Product product;
    private List<Product> products = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        Product product1 = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY, 1L);
        ReflectionTestUtils.setField(product, "id", PRODUCT_ID);

        this.products.add(product1);
        Product product2 = Product.of(PRODUCT_NAME + "2", PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY, 1L);
        ReflectionTestUtils.setField(product2, "id", PRODUCT_ID);
        this.products.add(product2);

        this.product = product1;
    }
    @DisplayName("주문 아이템 생성 테스트")
    @Test
    public void testOrderItemCreation() {
        // arrange
//        UserModel userModel = new UserModel("testuser", "shwlsdn@naver.com", "2001-01-01", "M");
//        ReflectionTestUtils.setField(userModel, "id", 1L);
        int quantity = 2;

        // act
        OrderItem orderItem = OrderItem.createOrderItem(product, quantity);

        // assert
        assertThat(orderItem.getProduct()).isEqualTo(product);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getOrderPrice()).isEqualTo(PRODUCT_PRICE.multiply(BigDecimal.valueOf(quantity)));
    }

    @DisplayName("주문 아이템 생성시 상품의 재고 보다 주문 수량이 많은 경우 illegalArgumentException 발생")
    @Test
    void throwsIllegalArgumentException_WhenQuantityIsGreaterThanAvailableQuantity() {
        // act
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
            OrderItem.createOrderItem(product, product.getQuantity() + 1);
        });
        // assert
        assertThat(result.getMessage()).isEqualTo("주문 수량이 재고 수량보다 많습니다.");
        assertThat(result.getClass()).isEqualTo(IllegalArgumentException.class);


    }
    @DisplayName("주문 아이템 생성시 상품이 null 이거나 수량이 0 이하인 경우 illegalArgumentException 발생")
    @Test
    void throwsIllegalArgumentException_WhenProductIsNullOrQuantityIsZeroOrNegative() {
        // act
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
            OrderItem.createOrderItem(null, 0);
        });
        // assert
        assertThat(result.getMessage()).isEqualTo("유효 하지 않은 상품 또는 수량입니다.");
        assertThat(result.getClass()).isEqualTo(IllegalArgumentException.class);
    }

}
