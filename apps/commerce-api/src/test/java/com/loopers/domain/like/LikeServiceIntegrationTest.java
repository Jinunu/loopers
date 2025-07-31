package com.loopers.domain.like;

import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.user.UserModel;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;



import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class LikeServiceIntegrationTest {

    private static final String PRODUCT_NAME = "신발";
    private static final String PRODUCT_IMAGE_URL = "https://example.com/image.jpg";
    private static final int PRODUCT_PRICE = 10000;
    private static final int PRODUCT_QUANTITY = 10;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private LikeService likeService;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;



    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("user가 상품에 좋아요를 누른다.")
    @Test
    public void likeProduct() {
        // arrange
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        productRepository.save(product);
        Product findProduct = productRepository.findByName(PRODUCT_NAME).orElseThrow(() -> new RuntimeException("Product not found"));

        UserModel userModel = new UserModel("testuser", "shwlsdn@naver.com", "2001-01-01", "M");
        userRepository.save(userModel);
        UserModel userModel1 = userRepository.findByUserId(userModel.getUserId());

        // act
        likeService.likeProduct(findProduct, userModel1);
        // assert
        List<Like> likeList = likeRepository.findByProductId(findProduct.getId());
        assertThat(likeList).hasSize(1);
        assertThat(likeList.get(0).getProduct().getId()).isEqualTo(findProduct.getId());
        assertThat(likeList.get(0).getUserModel().getId()).isEqualTo(userModel1.getId());
    }

    @DisplayName("user가 좋아요를 한 상품을 한번 더 좋아요를 요청하는 경우 좋아요가 추가되지 않는다.")
    @Test
    void shouldNotAddLikeWhenUserLikesProductTwice() {
        // arrange
        Product product = Product.of(PRODUCT_NAME, PRODUCT_IMAGE_URL, PRODUCT_PRICE, PRODUCT_QUANTITY);
        productRepository.save(product);
        Product findProduct = productRepository.findByName(PRODUCT_NAME).orElseThrow(() -> new RuntimeException("Product not found"));

        UserModel userModel = new UserModel("testuser", "shwlsdn@naver.com", "2001-01-01", "M");
        userRepository.save(userModel);
        UserModel userModel1 = userRepository.findByUserId(userModel.getUserId());

        // act
        likeService.likeProduct(findProduct, userModel1);
        assertThrows(CoreException.class, () -> {
            likeService.likeProduct(findProduct, userModel1);
        });

        List<Like> likeList = likeRepository.findByProductId(findProduct.getId());
        assertThat(likeList).hasSize(1);

    }

}
