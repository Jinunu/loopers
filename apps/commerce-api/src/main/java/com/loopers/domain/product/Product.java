package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
public class Product  extends BaseEntity {


    private String name;
    private String imageUrl;
    private BigDecimal price;
    private int quantity;
    @Column(name = "brand_id", nullable = false)
    private Long brandId;
    protected Product() {
    }

    protected Product(String name, String imageUrl, BigDecimal price, int quantity, Long brandId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        if (quantity < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST);
        }
        this.quantity = quantity;
        this.brandId = brandId;
    }


    public static Product of(String name, String imageUrl, BigDecimal price, int quantity, Long brandId) {

        return new Product(name, imageUrl, price, quantity, brandId);
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

    public void increaseProductQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
        this.quantity += quantity;
    }
}
