package com.loopers.domain.point;


public interface PointRepository {
    PointEntity findByUserId(String userId);

    void save(PointEntity point);

    void chargePoint(String userPoint, Long amount);
}
