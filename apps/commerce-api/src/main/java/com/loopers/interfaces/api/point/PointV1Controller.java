package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointV1Controller implements PointV1ApiSpec {

    private final PointFacade pointFacade;

    @Override
    @GetMapping
    public ApiResponse<PointV1Dto.PointResponse> getPoint(@RequestHeader(name = "X-USER-ID", required = false) String userId)
    {

        PointV1Dto.PointResponse response = PointV1Dto.PointResponse.from(pointFacade.getPoint(userId));
        return ApiResponse.success(response);
    }
}
