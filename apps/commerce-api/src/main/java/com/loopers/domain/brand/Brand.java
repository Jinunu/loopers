package com.loopers.domain.brand;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "brand")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Brand extends BaseEntity {

    private String name;
    private String imageUrl;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_id")
    private final List<Product> products = new ArrayList<>();

    protected Brand(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static Brand of(String name, String imageUrl) {
        return new Brand(name, imageUrl);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Optional<Product> findProductById(Long productId) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
    }

}
