package com.loopers.domain.product;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Products {
    private final List<Product> products = new ArrayList<>();


    public void addProduct(Product product) {
      products.add(product);
    }

    public int count() {
        return products.size();
    }
}
