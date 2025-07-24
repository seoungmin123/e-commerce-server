package kr.hhplus.be.server.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User 도메인 단위 테스트")
class UserTest {

    @Test
    void 사용자_생성_정상() {
        User user = new User(1L,1000L);

        assertEquals(1000L, user.getPoint());
        assertNotNull(user.getCreatedDt());
    }

    @Test
    void 포인트_충전_정상() {
        User user = new User(1L,500L);
        user.chargePoint(300L);

        assertEquals(800L, user.getPoint());
    }

    @Test
    void 포인트_충전_실패_null_금액() {
        User user = new User(1L,500L);

        assertThrows(IllegalArgumentException.class, () -> user.chargePoint(null));
    }

    @Test
    void 포인트_사용_정상() {
        User user = new User(1L,1000L);
        user.usePoint(600L);

        assertEquals(400L, user.getPoint());
    }

    @Test
    void 포인트_사용_실패_잔액부족() {
        User user = new User(1L,1000L);

        assertThrows(IllegalArgumentException.class, () -> user.usePoint(2000L));
    }


    @Test
    void 포인트_충전_정상_처리() {
        // given
        User user = new User(1L,1000L);

        // when
        user.chargePoint(500L);

        // then
        assertThat(user.getPoint()).isEqualTo(1500L);
    }

    @Test
    void 포인트_충전_금액_0_또는_음수_예외() {
        User user = new User(1L,1000L);

        assertThatThrownBy(() -> user.chargePoint(0L))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> user.chargePoint(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 포인트_사용_정상_처리() {
        User user = new User(1L,1000L);

        user.usePoint(500L);

        assertThat(user.getPoint()).isEqualTo(500L);
    }

    @Test
    void 포인트_사용_금액이_잔액_초과_시_예외() {
        User user = new User(1L,500L);

        assertThatThrownBy(() -> user.usePoint(1000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잔액 부족");
    }
}