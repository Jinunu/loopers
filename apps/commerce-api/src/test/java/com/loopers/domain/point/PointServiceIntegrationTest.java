package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class PointServiceIntegrationTest {

    /**
     * - [ ]  해당 ID 의 회원이 존재할 경우, 보유 포인트가 반환된다.
     * - [ ]  해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.
     */

    @Autowired
    private PointService pointService;

    @MockitoSpyBean
    private PointRepository pointRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        pointRepository.save(new PointEntity("shwlsdn", 1000L));
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("포인트 조회")
    @Nested
    class get {

        @DisplayName("해당 ID의 회원이 존재할 경우, 보유 포인트가 반환 된다.")
        @Test
        void returnPoint_whenUserExists() {
            // arrange
            String userId = "shwlsdn";
            // act
            PointEntity point = pointService.getPointByUserId(userId);

            // assert
            assertAll(
                    () -> assertThat(point.getId()).isNotNull(),
                    () -> assertThat(point.getUserId()).isEqualTo(userId),
                    () -> assertThat(point.getAmount()).isNotNull()
            );
        }


        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnNull_whenUserNotExists() {
            // arrange
            String userId = "shassdsadwlsdn";
            // act
            PointEntity point = pointService.getPointByUserId(userId);

            // assert
            assertThat(point).isNull();
        }


        @DisplayName("포인트 충전")
        @Nested
        class chargePoint{

            @DisplayName("존재하지 않는 유저 ID 로 충전을 시도한 경우, 실패한다.")
            @Test
            void failsChargePoint_when(){

                //arrange
                String nonExistUserId = "nonExistUserId";
                doReturn(null).when(pointRepository).findByUserId(nonExistUserId);

                // act
                CoreException result = assertThrows(CoreException.class, () -> {
                    pointService.chargePoint(nonExistUserId, 1000L);
                });

                // assert
                assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);

            }

        }

    }


}
