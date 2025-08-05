package com.loopers.domain.order;

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

    private Long userId;

    private BigDecimal totalPrice;



    // 연관관계 편의 메서드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(List<OrderItem> orderItems, Long userId) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }

        Order order = new Order();
        order.status = OrderStatus.PENDING;
        order.userId = userId;

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


}
