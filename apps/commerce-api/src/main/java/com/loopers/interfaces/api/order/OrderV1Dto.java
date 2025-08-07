package com.loopers.interfaces.api.order;

import com.loopers.application.point.PointInfo;

import java.math.BigDecimal;
import java.util.Map;

public class OrderV1Dto {




    public record OrderRequestDto(
            Map<Long, Integer> orderItems,
            String shippingAddress
    ){
        public static OrderRequestDto from(Map<Long, Integer> orderItems, String shippingAddress) {
            return new OrderRequestDto(orderItems,  shippingAddress);
        }
    }
}
