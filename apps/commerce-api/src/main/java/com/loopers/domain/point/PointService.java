package com.loopers.domain.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public PointEntity getPointByUserId(String userId){
        return pointRepository.findByUserId(userId);
    }
}
