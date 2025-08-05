package com.loopers.domain.order;

public enum OrderStatus {
    PENDING,    // 주문 대기 중
    PROCESSING, // 주문 처리 중
    COMPLETED,  // 주문 완료
    CANCELLED,  // 주문 취소됨
    REFUNDED    // 환불됨
}
