package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderInfo;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderV1Controller implements OrderV1ApiSpec {
    private final OrderFacade orderFacade;

    @Override
    @PostMapping
    public ApiResponse<?> requestOrder(@RequestBody OrderV1Dto.OrderRequestDto orderRequestDto, @RequestHeader(name = "X-USER-ID", required = false) String userId) {
        OrderInfo orderInfo = OrderInfo.from(orderRequestDto, userId);
        orderFacade.processNewOrder(orderInfo);
        return ApiResponse.success();
    }
}
