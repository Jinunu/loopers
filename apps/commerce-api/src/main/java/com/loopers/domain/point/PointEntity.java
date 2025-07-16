package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointEntity extends BaseEntity {

    private String userId;
    private Long amount;

    void chargeAmount(Long amount) {
        if (amount <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "0이하의 포인트는 충전 할 수 없습니다.");
        }
        this.amount += amount;
    }
}
