package com.loopers.interfaces.api;


import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointRepository;
import com.loopers.interfaces.api.point.PointV1Dto;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:db/init-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PointV1ApiE2ETest {

    private static final String ENDPOINT = "/api/v1/points";
    private static final String HEADER = "X-USER-ID";


    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;


    @BeforeEach
    void setUp() {
        pointRepository.save(new PointEntity("chulsoo", new BigDecimal("1000")));
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("Get /api/v1/points")
    @Nested
    class getPoint {

        @DisplayName("포인트 조회에 성공할 경우, 보유 포인트를 응답으로 반환한다.")
        @Test
        void returnsPoint_whenSuccess() {
            // arrange

            String userId = "chulsoo";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HEADER, userId);

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType);

            // assert
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody().data().point()).isEqualTo(new BigDecimal("1000"))
            );

        }

        @DisplayName("`X-USER-ID` 헤더가 없을 경우, `400 Bad Request` 응답을 반환한다.")
        @Test
        void throwsBadRequest_whenUserIdHeaderNotPresent() {
            // arrange
            HttpHeaders httpHeaders = null;


            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType);

            // assert
            assertAll(
                    () -> assertTrue(response.getStatusCode().is4xxClientError()),
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                    () -> assertThat(response.getBody().data()).isNull()
            );

        }

        @DisplayName("POST /api/v1/points/charge")
        @Nested
        class chargePoint {

            @DisplayName("존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
            @Test
            void returnTotalPoint_whenChargePointForExistingUser() {
                // pointRepository.findByUserId에서 "chulsoo"라는 유저를 리턴 하는 테스트 객체 만든 후 테스트 진행
                // arrange
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HEADER, "chulsoo");
                PointV1Dto.PointChargeRequest pointChargeRequest = new PointV1Dto.PointChargeRequest(new BigDecimal("1000"));


                // act
                ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
                };
                ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT + "/charge", HttpMethod.POST, new HttpEntity<>(pointChargeRequest, httpHeaders), responseType);

                assertAll(
                        () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                        () -> assertThat(response.getBody().data().point()).isEqualTo(new BigDecimal("2000"))
                );
            }

            @DisplayName("존재하지 않는 유저로 요청할 경우, `400 Bad Request` 응답을 반환한다.")
            @Test
            void returnNotFound_whenChargePointForNonExistingUser() {
                PointV1Dto.PointChargeRequest pointChargeRequest = new PointV1Dto.PointChargeRequest(new BigDecimal("1000"));
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HEADER, "bbbbbb");


                ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
                };
                ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT + "/charge", HttpMethod.POST, new HttpEntity<>(pointChargeRequest, httpHeaders), responseType);

                assertAll(
                        () -> assertTrue(response.getStatusCode().is4xxClientError()),
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                        () -> assertThat(response.getBody().data()).isNull()
                );
            }

        }

    }
}
