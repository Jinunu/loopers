package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void signUp(UserModel userModel){
        if (findByUserId(userModel.getUserId()) != null){
            throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 ID 입니다.");
        }
        userRepository.save(userModel);
    }

    @Transactional(readOnly = true)
    public UserModel findByUserId(String userId){
        return userRepository.findByUserId(userId);
    }
}
