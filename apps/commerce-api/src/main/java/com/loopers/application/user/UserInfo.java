package com.loopers.application.user;

import com.loopers.domain.user.UserModel;

public record UserInfo(String userId, String email, String birthDate, String gender) {
    public static UserModel to(UserInfo userInfo){
        return new UserModel(
                userInfo.userId(),
                userInfo.email(),
                userInfo.birthDate(),
                userInfo.gender()
        );
    }

    public static UserInfo from(UserModel userModel) {
        return new UserInfo(
                userModel.getUserId(),
                userModel.getEmail(),
                userModel.getBirthDate(),
                userModel.getGender()
        );
    }
}
