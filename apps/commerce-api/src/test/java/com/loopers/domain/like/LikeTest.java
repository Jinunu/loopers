package com.loopers.domain.like;

import com.loopers.domain.product.Product;
import com.loopers.domain.user.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class LikeTest {



    @Test
    public void likeProduct() {
        // arrange
        Product product = Product.of("Test Product", "https://example.com/image.jpg", 10000, 10);
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
}
