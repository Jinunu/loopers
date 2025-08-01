package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Component
public class UserV1Controller implements UserV1ApiSpec{
    private final UserFacade userFacade;

    @PostMapping
    public ApiResponse<UserV1Dto.UserResponse> signUp(@RequestBody UserV1Dto.UserRequest userRequest) {
        userFacade.signUp(UserV1Dto.UserRequest.to(userRequest));
        UserInfo userInfo = userFacade.findByUserId(userRequest.userId());
        UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(userInfo);
        return ApiResponse.success(response);
    }

    @Override
    @GetMapping("/{userId}")
    public ApiResponse<UserV1Dto.UserResponse> myProfile(@PathVariable String userId) {
        UserInfo userInfo = userFacade.findByUserId(userId);
        UserV1Dto.UserResponse response = UserV1Dto.UserResponse.from(userInfo);
        return ApiResponse.success(response);
    }
}
