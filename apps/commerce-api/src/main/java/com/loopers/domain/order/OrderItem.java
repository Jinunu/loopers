package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private int quantity;
    private BigDecimal orderPrice;

    protected OrderItem(Product product, int quantity, BigDecimal orderPrice) {
        this.product = product;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
    }

    public static OrderItem createOrderItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("유효 하지 않은 상품 또는 수량입니다.");
        }
        if (product.getQuantity() - quantity < 0) {
            throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다.");
        }
        product.decreaseQuantity(quantity);
        BigDecimal orderPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        return new OrderItem(product, quantity, orderPrice);
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
