package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {
    private final PointJpaRepository pointJpaRepository;


    @Override
    public PointEntity findByUserId(String userId) {
        return pointJpaRepository.findByUserId(userId);
    }

    @Override
    public void save(PointEntity point) {
        pointJpaRepository.save(point);
    }

    @Override
    public void chargePoint(String userPoint, Long amount) {

    }

}
