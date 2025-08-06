package com.loopers.domain.point;


public interface PointRepository {
    PointEntity findByUserId(String userId);

    PointEntity save(PointEntity point);

}
