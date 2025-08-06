package com.loopers.domain.order;

import com.loopers.application.order.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    // 주문 생성
    @Transactional
    public Order createOrder(OrderForm orderForm) {
        Order order = Order.createOrder(orderForm);

        return orderRepository.save(order);
    }




}
