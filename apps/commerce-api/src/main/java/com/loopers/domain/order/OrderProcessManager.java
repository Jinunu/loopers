package com.loopers.domain.order;

import com.loopers.application.order.OrderForm;
import com.loopers.application.order.OrderRequest;
import com.loopers.application.point.PointInfo;
import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderProcessManager {
    private final OrderService orderService;
    private final ProductService productService;
    private final PointService pointService;


    // OrderForm으로 주문 생성
    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        Map<Long, Integer> orderItems = orderRequest.getOrderItems();
        String userId = orderRequest.getUserId();
        List<Long> productIds = new ArrayList<>(orderItems.keySet());

        List<Product> products = productService.findProductsByIds(productIds);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : orderItems.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            Product product = products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + productId));
            orderItemList.add(OrderItem.createOrderItem(product, quantity));
        }
        OrderForm orderForm = new OrderForm(userId, orderRequest.getShippingAddress(), orderItemList);
        Order order = orderService.createOrder(orderForm);

        usagePoint(order);

        return order;
    }

    public void usagePoint(Order order) {
        order.createPayment();
        PointEntity point = pointService.getPointByUserId(order.getUserId());
        BigDecimal totalPrice = order.getTotalPrice();
        BigDecimal userPoint = point.getAmount();
        if (userPoint.compareTo(totalPrice) >= 0) {
            point.usePoint(totalPrice);
            order.getPayment().updatePaymentStatus(PaymentStatus.COMPLETED);
            order.updateStatus(OrderStatus.COMPLETED);
        }else {
            order.updateStatus(OrderStatus.PENDING);
            order.getPayment().updatePaymentStatus(PaymentStatus.FAILED);
        }

    }
    @Transactional
    public void cancelOrder(Long orderId, String userId) {
        orderService.findById(orderId).ifPresent(order -> {
            // 재고 증감
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                int quantity = orderItem.getQuantity();
                product.increaseProductQuantity(quantity);
            }
            order.updateStatus(OrderStatus.CANCELLED);
            order.getPayment().updatePaymentStatus(PaymentStatus.REFUNDED);
            // 포인트 환급
            pointService.chargePoint(new PointInfo(userId, order.getTotalPrice()));
        });
    }
}
