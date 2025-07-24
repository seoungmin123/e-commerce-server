package kr.hhplus.be.server.pointHistory.domain;

import kr.hhplus.be.server.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PointHistory 도메인 테스트")
class PointHistoryTest {

    @Nested
    @DisplayName("정적 생성 메서드 - charge")
    class StaticFactoryCharge {

        @Test
        @DisplayName("포인트 충전 히스토리를 생성한다")
        void 포인트_충전_히스토리_생성() {
            // given
            User user = new User(1L, 0L);
            Long amount = 5000L;
            Long balanceAfter = 5000L;

            // when
            PointHistory history = PointHistory.charge(user, amount, balanceAfter);

            // then
            assertThat(history.getUser().getUserId()).isEqualTo(user.getUserId());
            assertThat(history.getAmount()).isEqualTo(amount);
            assertThat(history.getType()).isEqualTo(PointType.CHARGE);
            assertThat(history.getBalanceAfter()).isEqualTo(balanceAfter);
            assertThat(history.getCreatedDt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("정적 생성 메서드 - use")
    class StaticFactoryUse {

        @Test
        @DisplayName("포인트 사용 히스토리를 생성한다")
        void 포인트_사용_히스토리_생성() {
            // given
            User user = new User(1L, 10000L);
            Long amount = 3000L;
            Long balanceAfter = 7000L;

            // when
            PointHistory history = PointHistory.use(user, amount, balanceAfter);

            // then
            assertThat(history.getUser().getUserId()).isEqualTo(user.getUserId());
            assertThat(history.getAmount()).isEqualTo(amount);
            assertThat(history.getType()).isEqualTo(PointType.USE);
            assertThat(history.getBalanceAfter()).isEqualTo(balanceAfter);
            assertThat(history.getCreatedDt()).isNotNull();
        }
    }
}