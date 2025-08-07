package com.loopers.interfaces.api.order;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "주문 V1 API", description = "주문 API")
public interface OrderV1ApiSpec {

    @Operation(
            summary = "주문 요청",
            description = "주문 요청"
    )
    ApiResponse<?> requestOrder(OrderV1Dto.OrderRequestDto orderRequest, String userId);
}
