package com.loopers.application.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Map<Long, Integer> orderItems;
    private String userId;
    private String shippingAddress;

}
