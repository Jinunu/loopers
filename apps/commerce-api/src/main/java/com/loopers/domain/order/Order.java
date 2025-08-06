package com.loopers.domain.order;

import com.loopers.application.order.OrderForm;
import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Order extends BaseEntity {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private String userId;
    private String shippingAddress;

    private BigDecimal totalPrice;



    // 연관관계 편의 메서드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(OrderForm orderForm) {
        List<OrderItem> orderItems = orderForm.getOrderItems();
        String shippingAddress = orderForm.getShippingAddress();
        String userId = orderForm.getUserId();
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }
        if (shippingAddress == null || shippingAddress.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 배송 주소입니다.");
        }

        Order order = new Order();
        order.status = OrderStatus.PENDING;
        order.userId = userId;
        order.shippingAddress = shippingAddress;

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
            if (orderItem.getProduct() == null || orderItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("유효하지 않은 주문 항목입니다.");
            }
            BigDecimal orderPrice = orderItem.getOrderPrice();
            if (orderPrice == null || orderPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("주문 가격이 유효하지 않습니다.");
            }

        }

        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getOrderPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);
        return order;
    }

    private void setTotalPrice(BigDecimal totalPrice) {
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("유효하지 않은 총 가격입니다.");
        }
        this.totalPrice = totalPrice;
    }
    public void updateStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("유효하지 않은 주문 상태입니다.");
        }
        this.status = status;
    }


}
