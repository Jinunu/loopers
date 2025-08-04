package com.loopers.domain.user;

public interface UserRepository {
    UserModel save(UserModel userModel);

    UserModel findByUserId(String userId);
}
