package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class PointEntityTest {

    @DisplayName("포인트 충전 테스트")
    @Nested
    class PointChargeTest {
        @DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다.")
        @ParameterizedTest
        @ValueSource(longs = {-1L, 0L, -333333L})
        void failsPointCharge_whenAmountIsNotPositive(Long amount){
            // arrange
            PointEntity point = new PointEntity("userId", 0L);

            // act
            CoreException result = assertThrows(CoreException.class, () -> {
                point.chargeAmount(amount);
            });

            // assert
            assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

    }
}
