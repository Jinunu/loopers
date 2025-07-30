package com.loopers.domain.brand;

import com.loopers.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class BrandTest {

    @DisplayName("브랜드 생성")
    @Test
    void createBrand() {
        String name = "나이스";
        String imageUrl = "https://example.com/image.jpg";
        Brand brand = Brand.of(name, imageUrl);
        assertThat(brand.getName()).isEqualTo(name);
        assertThat(brand.getImageUrl()).isEqualTo(imageUrl);
    }

    @DisplayName("브랜드에 상품 추가시 정상적으로 추가 된다.")
    @Test
    void addProductToBrand_When_AddingNewProduct() {
        //arrange

        String productImageUrl = "https://example.com/image.jpg";
        String brandImageUrl = "https://example.com/image.jpg";
        String name = "신발";
        int price = 10000;
        int quantity = 10;

        Product product = Product.of(name, productImageUrl, price, quantity);
        ReflectionTestUtils.setField(product, "id", 3L);

        String brandName = "나이스";
        Brand brand = Brand.of(brandName, brandImageUrl);
        ReflectionTestUtils.setField(brand, "id", 1L);

        //act
        brand.addProduct(product);
        int  productCount = brand.getProducts().size();

        // assert
        assertThat(productCount).isEqualTo(1);

    }

}
