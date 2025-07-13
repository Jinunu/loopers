package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "User API 입니다.")
public interface UserV1ApiSpec {

    @Operation(
        summary = "회원 가입",
        description = "ID, 이메일, 생년월일, 성별"
    )
    ApiResponse<UserV1Dto.UserResponse> signUp(
        @Schema(name = "회원가입", description = "회원가입")
        UserV1Dto.UserRequest userRequest
    );
}
