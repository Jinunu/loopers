package com.loopers.application.user;

import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
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
        if (userModel == null) {
            throw new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 ID 입니다.");
        }
        return UserInfo.from(userModel);
    }

}
