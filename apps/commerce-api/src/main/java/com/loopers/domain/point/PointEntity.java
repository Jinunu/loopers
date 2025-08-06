package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointEntity extends BaseEntity {

    @Column(unique = true)
    private String userId;
    private BigDecimal amount;

    void chargeAmount(BigDecimal amount) {
        if (amount == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "충전 금액은 필수 값 입니다.");
        }
        BigDecimal roundedAmount = amount.setScale(0, RoundingMode.HALF_UP);

        if (roundedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "0이하의 포인트는 충전 할 수 없습니다.");
        }

        this.amount = this.amount.add(roundedAmount);
    }

    public void usePoint(BigDecimal totalPrice) {
        BigDecimal roundedPrice = totalPrice.setScale(0, RoundingMode.HALF_UP);

        this.amount = this.amount.subtract(roundedPrice);
    }
    public BigDecimal getAmount()  {
      return this.amount = this.amount.setScale(0, RoundingMode.HALF_UP);
    }
}
