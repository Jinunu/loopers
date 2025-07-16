package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "포인트 V1 API", description = "포인트 API")
public interface PointV1ApiSpec {

    @Operation(
            summary = "포인트 조회",
            description = "회원 ID로 포인트 조회"

    )
    ApiResponse<PointV1Dto.PointResponse> getPoint(String userId);
}
