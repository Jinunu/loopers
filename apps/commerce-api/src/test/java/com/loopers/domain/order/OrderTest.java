package com.loopers.domain.order;

import com.loopers.application.order.OrderForm;
import com.loopers.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {
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
        ReflectionTestUtils.setField(product1, "id", PRODUCT_ID);

        this.products.add(product1);
        Product product2 = Product.of(PRODUCT_NAME + "2", PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY, 1L);
        ReflectionTestUtils.setField(product2, "id", PRODUCT_ID);
        this.products.add(product2);

        this.product = product1;
    }


    @DisplayName("주문 생성 테스트_결제전_주문대기 ")
    @Test
    public void createOrder_Status_PENDING() {
        // arrange
        List<OrderItem> orderItems = new ArrayList<>();
        for (Product product : products) {
            OrderItem orderItem = OrderItem.createOrderItem(product, 1);
            orderItems.add(orderItem);
        }

        // act
        String userId = "chulsoo"; // 예시 사용자 ID
        Order order = Order.createOrder(new OrderForm(
                userId,
                "서울시 강남구 역삼동 123-45", // 예시 배송 주소
                orderItems
        ));
        // assert
        assertThat(order.getOrderItems()).hasSize(2);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getTotalPrice()).isEqualTo(
                orderItems.stream()
                        .map(OrderItem::getOrderPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        assertThat(order.getUserId()).isEqualTo(userId);
        assertThat(order.getPayment()).isNull(); // 결제 정보는 아직 설정되지 않

    }
}
