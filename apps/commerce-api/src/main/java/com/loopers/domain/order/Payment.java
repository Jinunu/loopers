package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "payment")
public class Payment  extends BaseEntity {

    private String userId;
    @OneToOne(mappedBy = "payment")
    private Order order;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private BigDecimal paymentAmount;

    protected Payment() {
        // Default constructor for JPA
    }

    public Payment(String userId, PaymentStatus paymentStatus, BigDecimal paymentAmount) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다.");
        }

        this.userId = userId;
        this.paymentStatus = paymentStatus;
        this.paymentAmount = paymentAmount;
    }

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
