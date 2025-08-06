package com.loopers.application.order;

import com.loopers.domain.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderForm {
    private String userId;

    private String shippingAddress;
    private List<OrderItem> orderItems;

}
