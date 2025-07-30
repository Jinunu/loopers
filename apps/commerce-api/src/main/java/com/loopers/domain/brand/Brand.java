package com.loopers.domain.brand;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.Products;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Brand {
    private Long id;
    private String name;
    private String imageUrl;
    private final Products products = new Products();

    protected Brand(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }


    public static Brand of(String name, String imageUrl) {
        return new Brand(name, imageUrl);
    }

    public void addProduct(Product product) {
        products.addProduct(product);
    }
}
