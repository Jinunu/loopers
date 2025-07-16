package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public PointEntity getPointByUserId(String userId){
        PointEntity point = pointRepository.findByUserId(userId);
        return pointRepository.findByUserId(userId);
    }

    @Transactional
    public PointEntity chargePoint(String userId, Long amount) {
        PointEntity point = pointRepository.findByUserId(userId);
        if (point == null) {
          throw new CoreException(ErrorType.NOT_FOUND);
        }

        return null;
    }
}
