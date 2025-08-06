package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointInfo;

import java.math.BigDecimal;

public class PointV1Dto {
    public record PointResponse(BigDecimal point) {
        public static PointResponse from(PointInfo pointInfo) {
            return new PointResponse(pointInfo.point());
        }
    }

    public record PointChargeRequest(BigDecimal point) {
        public static PointChargeRequest from(BigDecimal point) {
            return new PointChargeRequest(point);
        }
    }
}
