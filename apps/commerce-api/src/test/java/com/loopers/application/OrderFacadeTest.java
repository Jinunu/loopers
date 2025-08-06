package com.loopers.application;

import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderRequest;
import com.loopers.application.point.PointInfo;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.codec.CodecException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql(scripts = "classpath:db/init-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:db/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderFacadeTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PointService pointService;

    @Autowired
    private OrderFacade orderFacade;


    @DisplayName("주문 생성 테스트")
    @Test
    @Transactional
    public void createOrderTest() {
        //arange
        String userId = "chulsoo";
        Map<Long, Integer> orderItemMap = new HashMap<>();

        orderItemMap.put(1L, 2); // 상품 ID 1번을 2개 주문
        orderItemMap.put(2L, 1); // 상품 ID 2번을 1개 주문
        Product product1 = productRepository.findById(1L).get();
        Product product2 = productRepository.findById(2L).get();

        int product1Quantity = product1.getQuantity();
        int product2Quantity = product2.getQuantity();


        OrderRequest orderRequest = new OrderRequest(orderItemMap, userId, "서울시 강남구");
        pointService.chargePoint(new PointInfo(userId, new BigDecimal("1000000")));
        //act
        orderFacade.processNewOrder(orderRequest);


        //assert
        BigDecimal totalPrice = BigDecimal.ZERO;
        orderRepository.findByUserId(userId).ifPresent(order -> {
            assertThat(order.getUserId()).isEqualTo(userId);
            assertThat(order.getShippingAddress()).isEqualTo("서울시 강남구");
            assertThat(order.getOrderItems().size()).isEqualTo(2);
            assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        });

        // 상품 재고 확인
        productRepository.findById(1L).ifPresent(product -> {
            assertThat(product.getQuantity()).isEqualTo(product1Quantity - 2);
        });
        productRepository.findById(2L).ifPresent(product -> {
            assertThat(product.getQuantity()).isEqualTo(product2Quantity - 1);
        });
    }

    @DisplayName("주문 요청시 유효하지 않은 주문 항목이 포함된 경우 예외 발생")
    @Test
    @Transactional
    public void createOrderWithInvalidItemsTest() {
        // arrange
        String userId = "chulsoo";
        Map<Long, Integer> orderItemMap = new HashMap<>();
        orderItemMap.put(1L, 0); // 상품 ID 1번을 0개 주문 (유효하지 않은 수량)

        OrderRequest orderRequest = new OrderRequest(orderItemMap, userId, "서울시 강남구");

        // act
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
            orderFacade.processNewOrder(orderRequest);
        });

        // assert
        assertThat(result.getMessage()).contains("유효 하지 않은 상품 또는 수량입니다.");

    }

    @DisplayName("주문 요청시 사용자 포인트가 부족한 경우 상품은 주문되지만 주문 상태는 PENDING")
    @Test
    @Transactional
    public void createOrderWithInsufficientPointsTest() {
        // arrange
        String userId = "chulsoo";
        Map<Long, Integer> orderItemMap = new HashMap<>();
        orderItemMap.put(1L, 2); // 상품 ID 1번을 2개 주문

        OrderRequest orderRequest = new OrderRequest(orderItemMap, userId, "서울시 강남구");
        pointService.chargePoint(new PointInfo(userId, new BigDecimal("100"))); // 포인트를 100으로 설정

        // act
        CoreException result = assertThrows(CoreException.class, () -> {
            orderFacade.processNewOrder(orderRequest);
        });

        // assert
        assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);

        orderRepository.findByUserId(userId).ifPresent(order -> {
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
            assertThat(order.getTotalPrice()).isGreaterThan(new BigDecimal("100"));
        });
    }


    @DisplayName("주문 요청시 상품의 재고가 부족한 경우 예외 발생")
    @Test
    @Transactional
    public void createOrderWithInsufficientStockTest() {
        // arrange
        String userId = "chulsoo";
        Map<Long, Integer> orderItemMap = new HashMap<>();
        orderItemMap.put(1L, 100); // 상품 ID 1번을 100개 주문 (재고 부족)

        OrderRequest orderRequest = new OrderRequest(orderItemMap, userId, "서울시 강남구");

        // act
        IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> {
            orderFacade.processNewOrder(orderRequest);
        });

        // assert
        assertThat(result.getMessage()).contains("주문 수량이 재고 수량보다 많습니다.");
        assertThat(orderRepository.findByUserId(userId)).isEmpty();

    }

}
