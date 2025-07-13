package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api/v1")
@Component
public class UserV1Controller implements UserV1ApiSpec{
    private final UserFacade userFacade;

    @PostMapping("users")
    public ApiResponse<UserV1Dto.UserResponse> signUp(UserV1Dto.UserRequest userRequest) {
        userFacade.signUp(UserV1Dto.UserRequest.to(userRequest));
        UserInfo userInfo = userFacade.findByUserId(userRequest.userId());
        UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(userInfo);
        return ApiResponse.success(response);
    }
}
