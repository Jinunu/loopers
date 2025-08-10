package com.loopers.domain.product;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface ProductInfoProjection {
    Long getProductId();
    String getProductName();
    String getProductImageUrl();
    BigDecimal getPrice();
    int getQuantity();
    ZonedDateTime getCreatedAt();

    Long getBrandId();
    String getBrandName();
    String getBrandImageUrl();

    int getLikeCount();
    boolean getLiked();

}
