package com.loopers.application.point;

import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointFacade {
    private final PointService pointService;

    public PointInfo getPoint(String userId){
        PointEntity point = pointService.getPointByUserId(userId);

        if (point == null) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        return PointInfo.from(point);
    }
}
