package com.loopers.interfaces.api;

import com.loopers.interfaces.api.product.ProductV1Dto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:db/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:db/init-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProductV1ApiE2ETest {

    private static final String ENDPOINT = "/api/v1/products";
    private static final String HEADER = "X-USER-ID";

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("GET /api/v1/products/{id}")
    @Nested
    class getProductDetail {
        @Test
        @DisplayName("상품ID로 상품 상세 정보를 조회 한다.")
        void getProductDetail_success() {
            // arrange
            HttpHeaders headers = new HttpHeaders();
            headers.add(HEADER, "1");

            // act
            ResponseEntity<ApiResponse<ProductV1Dto.ProductDetailResponse>> response = restTemplate.exchange(
                    ENDPOINT + "/1",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {}
            );

            // assert
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().productId()).isEqualTo(1L),
                    () -> assertThat(response.getBody().data().productName()).isNotBlank(),
                    () -> assertThat(response.getBody().data().brandId()).isNotNull(),
                    () -> assertThat(response.getBody().data().likeCount()).isGreaterThanOrEqualTo(0)
            );
        }
    }

    @DisplayName("GET /api/v1/products")
    @Nested
    class getProductList {
        @Test
        @DisplayName("상품 목록 조회시 각 상품의 좋아요 수가 표시 되고, 기본 정렬은 최신순이다.")
        void list_default_latestDesc() {
            // arrange
            HttpHeaders headers = new HttpHeaders();
            headers.add(HEADER, "1");

            // act
            ResponseEntity<ApiResponse<PageResponse<ProductV1Dto.ProductListItem>>> response = restTemplate.exchange(
                    ENDPOINT,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {}
            );

            // assert
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().content()).isNotEmpty(),
                    () -> assertThat(response.getBody().data().content().get(0).createdAt())
                            .isAfterOrEqualTo(response.getBody().data().content().get(1).createdAt()),
                    () -> assertThat(response.getBody().data().content().get(0).likeCount()).isGreaterThanOrEqualTo(0)
            );
        }

        @Test
        @DisplayName("가격 오름차순 정렬")
        void list_price_asc() {
            // arrange
            HttpHeaders headers = new HttpHeaders();
            headers.add(HEADER, "1");

            // act
            ResponseEntity<ApiResponse<PageResponse<ProductV1Dto.ProductListItem>>> response = restTemplate.exchange(
                    ENDPOINT + "?sortField=price&sortDirection=asc&page=0&size=10",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {}
            );

            // assert
            var list = response.getBody().data().content();
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(list).isNotEmpty(),
                    () -> assertThat(list.get(0).price()).isLessThanOrEqualTo(list.get(1).price())
            );
        }

        @Test
        @DisplayName("가격 내림차순 정렬")
        void list_price_desc() {
            // arrange
            HttpHeaders headers = new HttpHeaders();
            headers.add(HEADER, "1");

            // act
            ResponseEntity<ApiResponse<PageResponse<ProductV1Dto.ProductListItem>>> response = restTemplate.exchange(
                    ENDPOINT + "?sortField=price&sortDirection=desc&page=0&size=10",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {}
            );

            // assert
            var list = response.getBody().data().content();
            assertAll(
                    () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                    () -> assertThat(list).isNotEmpty(),
                    () -> assertThat(list.get(0).price()).isGreaterThanOrEqualTo(list.get(1).price())
            );
        }
    }
}
