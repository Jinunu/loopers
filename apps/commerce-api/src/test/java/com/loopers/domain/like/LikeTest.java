package com.loopers.domain.like;

import com.loopers.domain.product.Product;
import com.loopers.domain.user.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class LikeTest {



    @Test
    public void likeProduct() {
        // arrange
        Product product = Product.of("Test Product", "https://example.com/image.jpg", new BigDecimal("10000"), 10, 1L);
        ReflectionTestUtils.setField(product, "id", 1L);
        UserModel userModel = new UserModel("testuser", "shwlsdn@naver.com", "2001-01-01", "M");
        ReflectionTestUtils.setField(userModel, "id", 1L);

        // act
        Like like = Like.likeProduct(product, userModel);

        // assert
        assertNotNull(like);
        assertEquals(product, like.getProduct());
        assertEquals(userModel, like.getUserModel());
    }

    @DisplayName("isLiked 메서드는 같은 유저일 경우 true를 반환한다")
    @Test
    public void isLiked_ReturnsTrue_WhenUserIsSame() {
        // arrange
        Product product = Product.of("Test Product", "https://example.com/image.jpg", new BigDecimal("10000"), 10, 1L);
        ReflectionTestUtils.setField(product, "id", 1L);
        UserModel userModel = new UserModel("testuser", "shwlsdn@naver.com", "2001-01-01", "M");
        ReflectionTestUtils.setField(userModel, "id", 1L);


        // act
        Like like = Like.likeProduct(product, userModel);
        boolean isLiked = like.isLiked(userModel);

        // assert
        assertThat(isLiked).isTrue();

    }

    @DisplayName("isLiked 메서드는 다른 유저일 경우 false를 반환한다")
    @Test
    public void isLiked_ReturnsFalse_WhenUserIsDifferent() {
        // arrange
        Product product = Product.of("Test Product", "https://example.com/image.jpg", new BigDecimal("10000"), 10, 1L);
        ReflectionTestUtils.setField(product, "id", 1L);
        UserModel userModel = new UserModel("testuser", "shwlsdn@naver.com", "2001-01-01", "M");
        ReflectionTestUtils.setField(userModel, "id", 1L);

        UserModel differentUser = new UserModel("testuser1", "shwlsdn1@naver.com", "2001-01-01", "M");
        ReflectionTestUtils.setField(differentUser, "id", 1L);
        // act
        Like like = Like.likeProduct(product, userModel);
        boolean isLiked = like.isLiked(differentUser);

        assertThat(isLiked).isFalse();
    }
}
