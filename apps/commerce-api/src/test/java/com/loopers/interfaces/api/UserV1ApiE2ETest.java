package com.loopers.interfaces.api;


import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserV1ApiE2ETest {

    private static final String ENDPOINT = "/api/v1/users";
    private static final Function<String, String> ENDPOINT_GET = userId -> "/api/v1/users/" + userId;

    private final TestRestTemplate testRestTemplate;
    private final UserJpaRepository userJpaRepository;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UserV1ApiE2ETest(
            TestRestTemplate testRestTemplate,
            UserJpaRepository userJpaRepository,
            DatabaseCleanUp databaseCleanUp
    ) {
        this.testRestTemplate = testRestTemplate;
        this.userJpaRepository = userJpaRepository;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/users")
    @Nested
    class save {
        @DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.")
        @Test
        void returnUserInfo_whenSignUpSuccessful() {
            // arrange
            UserV1Dto.UserRequest userRequest = new UserV1Dto.UserRequest("shwlsdn", "shwlsdn@go.kr", "1994-08-19", "M");

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(userRequest), responseType);

            // assert
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody().data().userId()).isEqualTo(userRequest.userId()),
                    () -> assertThat(response.getBody().data().email()).isEqualTo(userRequest.email()),
                    () -> assertThat(response.getBody().data().brithDate()).isEqualTo(userRequest.birthdate()),
                    () -> assertThat(response.getBody().data().gender()).isEqualTo(userRequest.gender())

            );

        }

        @DisplayName("회원 가입시 성별이 없을 경우, 400 Bad Request 응답을 받는다.")
        @Test
        void throwsBadRequest_whenGenderIsNotProvided() {
            // arrange
            UserV1Dto.UserRequest userRequest = new UserV1Dto.UserRequest("shwlsdn", "shwlsdn@go.kr", "1994-08-19", "");

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(userRequest), responseType);

            assertAll(
                    () -> assertTrue(response.getStatusCode().is4xxClientError()),
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST)
            );


        }
    }

    @DisplayName("내 정보 조회")
    @Nested
    class get {
        @DisplayName("내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환 받는다.")
        @Test
        void returnUserInfo_whenUserExists() {
            // arrange
            UserModel userModel = new UserModel("shwlsdn", "shwlsdn@go.kr", "1994-08-19", "M");
            userJpaRepository.save(userModel);
            String requestUrl = ENDPOINT_GET.apply("shwlsdn");

            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(null), responseType);

            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody().data().userId()).isEqualTo(userModel.getUserId()),
                    () -> assertThat(response.getBody().data().email()).isEqualTo(userModel.getEmail()),
                    () -> assertThat(response.getBody().data().brithDate()).isEqualTo(userModel.getBirthDate()),
                    () -> assertThat(response.getBody().data().gender()).isEqualTo(userModel.getGender())

            );
        }

        @DisplayName("존재하지 않는 ID로 조회할 경우, 404 Not Found 응답을 받는다")
        @Test
        void throwsNotFound_whenNotExistUserId() {
            // arrange
            UserModel userModel = new UserModel("shwlsdn", "shwlsdn@go.kr", "1994-08-19", "M");
            userJpaRepository.save(userModel);
            String requestUrl = ENDPOINT_GET.apply("ㄴㄴㄴㄴ");

            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(null), responseType);

            assertAll(
                    () -> assertTrue(response.getStatusCode().is4xxClientError()),
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
            );


        }
    }


}
