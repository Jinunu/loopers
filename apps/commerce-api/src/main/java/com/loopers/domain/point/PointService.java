package com.loopers.domain.point;

import com.loopers.application.point.PointInfo;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
            PointEntity firstPoint = new PointEntity(pointInfo.userId(), pointInfo.point());
         return    pointRepository.save(firstPoint);
        }
        point.chargeAmount(pointInfo.point());
        pointRepository.save(point);
        return point;
    }

    public void usePoint(String userId, BigDecimal totalPrice) {


    }
}
