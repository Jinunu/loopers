package com.loopers.application.point;

import com.loopers.domain.point.PointEntity;
import com.loopers.interfaces.api.point.PointV1Dto;

import java.math.BigDecimal;

public record PointInfo(String userId, BigDecimal point) {
    public static PointInfo from(PointEntity point) {
        return new PointInfo(point.getUserId(), point.getAmount());
    }

    public static PointInfo to(String userId, PointV1Dto.PointChargeRequest request) {
        return new PointInfo(userId, request.point());
    }
}
