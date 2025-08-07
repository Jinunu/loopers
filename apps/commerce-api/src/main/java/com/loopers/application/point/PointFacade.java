package com.loopers.application.point;

import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointFacade {
    private final PointService pointService;
    private final UserService userService;

    public PointInfo getPoint(String userId) {
        PointEntity point = pointService.getPointByUserId(userId);

        if (point == null) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        return PointInfo.from(point);
    }

    public PointInfo chargePoint(PointInfo pointInfo) {
        UserModel userModel = userService.findByUserId(pointInfo.userId());
        if (userModel == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "존재하지 않는 사용자입니다.");
        }
        PointEntity point = pointService.chargePoint(pointInfo);
        return PointInfo.from(point);
    }
}
