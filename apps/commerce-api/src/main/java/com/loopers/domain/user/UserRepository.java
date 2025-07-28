package com.loopers.domain.user;

public interface UserRepository {
   void save(UserModel userModel);

    UserModel findByUserId(String userId);
}
