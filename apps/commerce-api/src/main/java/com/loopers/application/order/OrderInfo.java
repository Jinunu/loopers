package com.loopers.application.order;

import com.loopers.interfaces.api.order.OrderV1Dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    private Map<Long, Integer> orderItems;
    private String userId;
    private String shippingAddress;

    public static OrderInfo from(OrderV1Dto.OrderRequestDto orderRequestDto, String userId) {
        return new OrderInfo(
                orderRequestDto.orderItems(),
                userId,
                orderRequestDto.shippingAddress()
        );
    }
}
