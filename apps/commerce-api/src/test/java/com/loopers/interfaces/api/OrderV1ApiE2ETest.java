package com.loopers.interfaces.api;


import com.loopers.domain.point.PointEntity;
import com.loopers.domain.point.PointRepository;
import com.loopers.interfaces.api.order.OrderV1Dto;
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
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:db/init-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OrderV1ApiE2ETest {

    private static final String ENDPOINT = "/api/v1/orders";
    private static final String HEADER = "X-USER-ID";


    @Autowired
    private PointRepository pointRepository;



    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;


    @BeforeEach
    void setUp() {
        pointRepository.save(new PointEntity("chulsoo", new BigDecimal("1000000")));
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/orders")
    @Nested
    class requestOrder {

        @DisplayName("주문 요청에 성공할 경우, Success 응답을 반환한다.")
        @Test
        void returnsSuccess_whenOrderRequestSuccessful() {
            // arrange
            String userId = "chulsoo";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HEADER, userId);

            String address = "서울시 강남구";
            Map<Long, Integer> orderedItems = new HashMap<>() {
                {
                    put(1L, 2); // 상품 ID 1번을 2개 주문
                    put(2L, 1); // 상품 ID 2번을 1개 주문
                }
            };

            OrderV1Dto.OrderRequestDto orderRequest = new OrderV1Dto.OrderRequestDto(orderedItems, "서울시 강남구");

            // act
            ResponseEntity<ApiResponse<?>> response = testRestTemplate.exchange(
                    ENDPOINT,
                    HttpMethod.POST,
                    new HttpEntity<>(orderRequest, httpHeaders),
                    new ParameterizedTypeReference<>() {}
            );

            // assert
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful())

            );
        }

        @DisplayName("주문시 상품의 재고가 부족한 경우 , 400 Bad Request 응답을 반환한다.")
        @Test
        void throwsBadRequest_whenInsufficientStock() {
            // arrange
            String userId = "chulsoo";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HEADER, userId);

            Map<Long, Integer> orderedItems = new HashMap<>() {
                {
                    put(1L, 100); // 상품 ID 1번을 100개 주문 (재고 부족)
                }
            };

            OrderV1Dto.OrderRequestDto orderRequest = new OrderV1Dto.OrderRequestDto(orderedItems, "서울시 강남구");

            // act
            ResponseEntity<ApiResponse<?>> response = testRestTemplate.exchange(
                    ENDPOINT,
                    HttpMethod.POST,
                    new HttpEntity<>(orderRequest, httpHeaders),
                    new ParameterizedTypeReference<>() {}
            );

            // assert
            assertAll(
                    () -> assertTrue(response.getStatusCode().is4xxClientError())
            );
        }
        @DisplayName("주문시 사용자의 포인트가 부족한 경우 , 400 Bad Request 응답을 반환한다.")
        @Test
        void throwsBadRequest_whenInsufficientPoint() {
            // arrange
            String userId = "chulsoo";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HEADER, userId);

            Map<Long, Integer> orderedItems = new HashMap<>() {
                {
                    put(1L, 40); // 상품 ID 1번을 2개 주문
                }
            };

            OrderV1Dto.OrderRequestDto orderRequest = new OrderV1Dto.OrderRequestDto(orderedItems, "서울시 강남구");

            // act
            ResponseEntity<ApiResponse<?>> response = testRestTemplate.exchange(
                    ENDPOINT,
                    HttpMethod.POST,
                    new HttpEntity<>(orderRequest, httpHeaders),
                    new ParameterizedTypeReference<>() {}
            );

            // assert
            assertAll(
                    () -> assertTrue(response.getStatusCode().is4xxClientError())
            );
        }
    }
}
