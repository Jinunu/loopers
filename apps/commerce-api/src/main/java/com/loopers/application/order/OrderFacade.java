package com.loopers.application.order;

import com.loopers.domain.order.*;
import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final ProductService productService;
    private final OrderProcessManager orderProcessManager;


    public void processNewOrder(OrderInfo orderInfo) {
        Order order = orderProcessManager.createOrder(orderInfo);
        if (order.getStatus() == OrderStatus.PENDING) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트가 부족합니다. 주문이 완료되지 않았습니다.");
        }


    }

    public void cancelOrder(Long orderId, String userId) {
        orderProcessManager.cancelOrder(orderId, userId);
    }
}
