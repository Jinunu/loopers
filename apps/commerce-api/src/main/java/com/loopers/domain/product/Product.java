package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.Getter;

@Getter
public class Product {

    private Long id;
    private String name;
    private String imageUrl;
    private int price;
    private int quantity;

    private Product() {
    }

    protected Product(String name, String imageUrl, int price, int quantity) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        if (quantity < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        this.quantity = quantity;
    }

    public static Product of(String name, String imageUrl, int price, int quantity) {
        return new Product(name, imageUrl, price, quantity);
    }

    public void decreaseQuantity(int quantity) {
        if (quantity <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }

        if (this.quantity < quantity) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        this.quantity -= quantity;
    }
}
