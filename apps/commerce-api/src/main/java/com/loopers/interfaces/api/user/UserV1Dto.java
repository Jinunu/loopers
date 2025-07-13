package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;

public class UserV1Dto {
    public record UserResponse(String userId, String email, String brithDate, String gender) {
        public static UserResponse from(UserInfo info) {
            return new UserResponse(
                    info.userId(),
                    info.email(),
                    info.birthDate(),
                    info.gender()
            );
        }
    }

    public record UserRequest(String userId, String email, String birthdate, String gender){
        public static UserInfo to(UserRequest request){
            return new UserInfo(
                    request.userId(),
                    request.email(),
                    request.birthdate(),
                    request.gender()
            );
        }
    }
}
