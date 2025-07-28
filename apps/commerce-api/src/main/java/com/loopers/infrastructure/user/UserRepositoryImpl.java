package com.loopers.infrastructure.user;

import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    public void save(UserModel userModel) {
        userJpaRepository.save(userModel);
    };

    public UserModel findByUserId(String userId){
        return userJpaRepository.findByUserId(userId);
    }
}
