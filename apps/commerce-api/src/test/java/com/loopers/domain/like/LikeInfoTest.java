package com.loopers.domain.like;

import com.loopers.domain.product.Product;
import com.loopers.domain.user.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class LikeInfoTest {
    private static final String PRODUCT_NAME = "신발";
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final int PRODUCT_PRICE = 10000;
    private static final int PRODUCT_QUANTITY = 10;
    private static final Long PRODUCT_ID = 123L;
    private static final String USER_NAME = "testuser";
    private static final String USER_EMAIL = "shwlsdn@naver.com";
    private static final String USER_BIRTH_DATE = "2001-01-01";
    private static final String GENDER = "M";
    private static final Long USER_ID = 1L;
    private static final int LIKE_COUNT = 500;

    @DisplayName("좋아요 정보 생성 테스트")
    @Test
    void createLikeInfoTest() {
        // arrange
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        ReflectionTestUtils.setField(product, "id", PRODUCT_ID);
        UserModel userModel = new UserModel(USER_NAME, USER_EMAIL, USER_BIRTH_DATE, GENDER);
        ReflectionTestUtils.setField(userModel, "id", USER_ID);
        Like like = Like.likeProduct(product, userModel);
        // act
        LikeInfo likeInfo = LikeInfo.from(like, LIKE_COUNT, userModel);

        // assert
        assertThat(likeInfo.getProductId()).isEqualTo(product.getId());
        assertThat(likeInfo.getIsLiked()).isTrue();
        assertThat(likeInfo.getLikeCount()).isEqualTo(LIKE_COUNT);

    }

    @DisplayName("좋아요 정보 생성 테스트 - 좋아요가 없는 경우")
    @Test
    void createLikeInfoWithoutLikeTest() {
        // arrange
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        ReflectionTestUtils.setField(product, "id", PRODUCT_ID);
        UserModel userModel = new UserModel(USER_NAME, USER_EMAIL, USER_BIRTH_DATE, GENDER);
        ReflectionTestUtils.setField(userModel, "id", USER_ID);
        // act
        LikeInfo likeInfo = LikeInfo.from(null, 0, userModel);
        // assert
        assertThat(likeInfo.getProductId()).isNull();
        assertThat(likeInfo.getIsLiked()).isFalse();
        assertThat(likeInfo.getLikeCount()).isEqualTo(0);

    }
}
