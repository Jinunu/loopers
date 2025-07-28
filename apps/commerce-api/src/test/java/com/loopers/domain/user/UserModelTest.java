package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserModelTest {

    @DisplayName("유저 모델 생성")
    @Nested
    class Create {

        @DisplayName("ID가 영문 및 숫자 10자 이내, 이메일이 xx@yy.zz, 생년월일이 yyyy-MM-dd, 성별이 비어있지 않은 경우 객체가 정상적으로 생성된다.")
        @Test
        void createsUserModel_whenAllFormatValidation(){
            String userId = "shwlsdn1";
            String email = "shwlsdn1@gmail.com";
            String birthDate = "1994-08-19";
            String gender = "M";
            UserModel userModel = new UserModel(userId, email, birthDate, gender);
            assertAll(
                    () -> assertThat(userModel.getId()).isNotNull(),
                    () -> assertThat(userModel.getUserId()).isEqualTo(userId),
                    () -> assertThat(userModel.getEmail()).isEqualTo(email),
                    () -> assertThat(userModel.getBirthDate()).isEqualTo(birthDate),
                    () -> assertThat(userModel.getGender()).isEqualTo(gender)
            );
        }



        @DisplayName("ID가 한글일 경우, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenUserIdContainsKoreanCharacters(){
            String email = "shwlsdn1@gmail.com";
            String birthDate = "1994-08-19";
            String gender = "M";
            CoreException result = assertThrows(CoreException.class, () -> {
                new UserModel("aa노진우1", email, birthDate, gender);

            });

            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("ID가 10자를 넘는 경우, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenIdLengthExceedsTenCharacters(){
            String email = "shwlsdn1@gmail.com";
            String birthDate = "1994-08-19";
            String gender = "M";
            CoreException result = assertThrows(CoreException.class, () -> {
                new UserModel("123456789aa", email, birthDate, gender);

            });

            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("email이 xx@yy.zz 형식에 맞지 않는 경우, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenEmailFormatIsInvalid(){
            String userId = "shwlsdn1";
            String birthDate = "1994-08-19";
            String gender = "M";
            CoreException resultContainKoreanCharacter = assertThrows(CoreException.class, () -> {
                new UserModel(userId, "노진우@aa.cc", birthDate, gender);
            });
            CoreException resultEmailBlank = assertThrows(CoreException.class, () -> {
                new UserModel(userId, " ", birthDate, gender);
            });
            CoreException resultInvalidEmail= assertThrows(CoreException.class, () -> {
                new UserModel(userId, "abce@aa", birthDate, gender);
            });

            assertAll(
                    () -> assertThat(resultContainKoreanCharacter.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST),
                    () -> assertThat(resultEmailBlank.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST),
                    () -> assertThat(resultInvalidEmail.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)

            );
        }

        @DisplayName("생년월일이 yyyy-MM-dd 형식에 맞지 않는 경우, BAD_REQUEST 예외가 발생한다.")
        @Test
        void throwsBadRequestException_whenBirthDateFormatIsInvalid(){
            String userId = "shwlsdn1";
            String email = "shwlsdn1@gmail.com";
            String gender = "M";

            CoreException resultEmailBlank = assertThrows(CoreException.class, () -> {
                new UserModel(userId, email, " ", gender);
            });
            CoreException resultDateFormat_yyyyMMdd= assertThrows(CoreException.class, () -> {
                new UserModel(userId, email, "19940819", gender);
            });

            assertAll(
                    () -> assertThat(resultEmailBlank.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST),
                    () -> assertThat(resultDateFormat_yyyyMMdd.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
            );
        }

    }


}
