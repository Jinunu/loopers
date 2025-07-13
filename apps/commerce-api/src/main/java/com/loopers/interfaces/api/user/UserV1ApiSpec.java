package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "User API 입니다.")
public interface UserV1ApiSpec {

    @Operation(
        summary = "회원 가입",
        description = "ID, 이메일, 생년월일, 성별 정보로 회원 가입"
    )
    ApiResponse<UserV1Dto.UserResponse> signUp(
        @Schema(name = "회원가입 요청 Dto", description = "회원가입 요청 Dto")
        UserV1Dto.UserRequest userRequest
    );

    @Operation(
            summary = "내 정보 조회",
            description = "회원의 ID로 회원 정보 조회"
    )
    ApiResponse<UserV1Dto.UserResponse> myProfile(
            @Schema(name = "회원의 ID", description = "조회 할 회원의 ID")
            String userId
    );
}
