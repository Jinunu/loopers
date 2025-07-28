package com.loopers.domain.point;


public interface PointRepository {
    PointEntity findByUserId(String userId);

    void save(PointEntity point);

}
