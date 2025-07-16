package com.loopers.application.point;

import com.loopers.domain.point.PointEntity;

public record PointInfo(Long point) {
    public static PointInfo from(PointEntity point) {
        return new PointInfo(point.getAmount());
    }
}
