package com.loopers.domain.point;

import com.loopers.application.point.PointInfo;
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
        return pointRepository.findByUserId(userId);
    }

    @Transactional
    public PointEntity chargePoint(PointInfo pointInfo) {
        PointEntity point = pointRepository.findByUserId(pointInfo.userId());
        if (point == null) {
          throw new CoreException(ErrorType.NOT_FOUND);
        }
        point.chargeAmount(pointInfo.point());
        pointRepository.save(point);
        return point;
    }
}
