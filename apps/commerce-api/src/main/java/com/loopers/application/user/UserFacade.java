package com.loopers.application.user;

import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFacade {
    private final UserService userService;

    public void signUp(UserInfo userInfo){
        userService.signUp(UserInfo.to(userInfo));
    }

    public UserInfo findByUserId(String userId){
        UserModel userModel = userService.findByUserId(userId);
        return UserInfo.from(userModel);
    }

}
