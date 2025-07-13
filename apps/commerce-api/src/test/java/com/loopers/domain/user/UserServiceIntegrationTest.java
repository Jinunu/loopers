package com.loopers.domain.user;

import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("회원 가입")
    @Nested
    class save {

        @DisplayName("회원 저장")
        @Test
        void signUpUser(){
            // arrange
            UserModel userModel = new UserModel("shwlsdn", "shwlsdn@co.kr", "1994-08-19", "M");

            //act
            userService.signUp(userModel);
            UserModel result = userJpaRepository.findByUserId(userModel.getUserId());

            // assert
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getId()).isNotNull(),
                    () -> assertThat(result.getUserId()).isEqualTo(userModel.getUserId()),
                    () -> assertThat(result.getEmail()).isEqualTo(userModel.getEmail()),
                    () -> assertThat(result.getBirthDate()).isEqualTo(userModel.getBirthDate()),
                    () -> assertThat(result.getGender()).isEqualTo(userModel.getGender())
            );

        }

        @DisplayName("이미 가입된 ID로 회원가입 시도 시, 실패한다.")
        @Test
        void failSignUp_whenUserIdAlreadyExists(){
            // arrange
            UserModel userModel = new UserModel("shwlsdn", "shwlsdn@co.kr", "1994-08-19", "M");
            UserModel userModel2 = new UserModel("shwlsdn", "shwlsdn@co.kr", "1994-08-19", "M");
            userService.signUp(userModel);

            // act
            CoreException exception = assertThrows(CoreException.class, () -> {
                userService.signUp(userModel2);
            });


            // assert
            assertThat(exception.getCustomMessage()).contains("이미 존재하는 ID");
        }
    }

    @DisplayName("내 정보 조회")
    @Nested
    class get {
        @DisplayName("해당 ID의 회원이 존재할 경우, 회원 정보가 반환된다.")
        @Test
        void returnUserInfo_whenExistUserId(){
            // arrange
            UserModel userModel = new UserModel("shwlsdn", "shwlsdn123@asd.cd", "1994-08-19", "M");
            userJpaRepository.save(userModel);

            // act
            UserModel result = userService.findByUserId("shwlsdn");

            // assert
            assertAll(
                    () -> assertThat(result.getUserId()).isEqualTo(userModel.getUserId()),
                    () -> assertThat(result.getEmail()).isEqualTo(userModel.getEmail()),
                    () -> assertThat(result.getBirthDate()).isEqualTo(userModel.getBirthDate()),
                    () -> assertThat(result.getGender()).isEqualTo(userModel.getGender())

            );
        }
    }

    @DisplayName("해당 ID의 회원이 존재하지 않을 경우, null을 반환 한다.")
    @Test
    void returnNull_whenNotExistUserId(){
        // arrange
        String userId = "abcedfg";

        // act
        UserModel result = userService.findByUserId(userId);

        // assert
        assertThat(result).isNull();
    }


}
