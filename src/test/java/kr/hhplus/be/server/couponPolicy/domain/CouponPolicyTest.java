package kr.hhplus.be.server.couponPolicy.domain;

import kr.hhplus.be.server.couponPolicy.exception.InsufficientCouponQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@DisplayName("CouponPolicy 도메인 테스트")
class CouponPolicyTest {

    private CouponPolicy couponPolicy;

    @BeforeEach
    void setUp() {
        couponPolicy = createCouponPolicy(10L, 5L, LocalDateTime.now().plusDays(1));
    }

    @Nested
    @DisplayName("만료 여부 확인 (isExpired)")
    class IsExpired {

        @Test
        @DisplayName("만료시간이 지나지 않았으면 false 반환")
        void 만료시간이_지나지않았으면_false() {
            assertThat(couponPolicy.isExpired()).isFalse();
        }

        @Test
        @DisplayName("만료시간이 지났으면 true 반환")
        void 만료시간이_지났으면_true() {
            setField(couponPolicy, "expiredDt", LocalDateTime.now().minusSeconds(1));
            assertThat(couponPolicy.isExpired()).isTrue();
        }
    }

    @Nested
    @DisplayName("수량 차감 (decreaseIssuedRemain)")
    class DecreaseIssuedRemain {

        @Test
        @DisplayName("수량이 존재하면 1 감소")
        void 수량이_존재하면_차감된다() {
            long before = couponPolicy.getRemainingQuantity();
            couponPolicy.decreaseIssuedRemain();
            assertThat(couponPolicy.getRemainingQuantity()).isEqualTo(before - 1);
        }

        @Test
        @DisplayName("수량이 없으면 예외 발생")
        void 수량이_없으면_차감시_예외발생() {
            setField(couponPolicy, "issuedRemain", 0L);
            assertThatThrownBy(() -> couponPolicy.decreaseIssuedRemain())
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("쿠폰 수량이 모두 소진되었습니다.");
        }
    }

    @Nested
    @DisplayName("수량 검증 (validateIssueAvailability)")
    class ValidateIssueAvailability {

        @Test
        @DisplayName("수량이 있으면 통과 (예외 없음)")
        void 수량검증_통과() {
            couponPolicy.validateIssueAvailability();
        }

        @Test
        @DisplayName("수량이 없으면 예외 발생")
        void 수량검증_실패_예외발생() {
            setField(couponPolicy, "issuedRemain", 0L);
            assertThatThrownBy(() -> couponPolicy.validateIssueAvailability())
                    .isInstanceOf(InsufficientCouponQuantityException.class);
        }
    }

    @Nested
    @DisplayName("수량 조회 관련")
    class Quantity {

        @Test
        @DisplayName("잔여 수량 반환")
        void 잔여수량조회() {
            assertThat(couponPolicy.getRemainingQuantity()).isEqualTo(5L);
        }

        @Test
        @DisplayName("수량 있으면 isQuantityAvailable 은 true")
        void 수량있으면_isQuantityAvailable_true() {
            assertThat(couponPolicy.isQuantityAvailable()).isTrue();
        }

        @Test
        @DisplayName("수량 없으면 isQuantityAvailable 은 false")
        void 수량없으면_isQuantityAvailable_false() {
            setField(couponPolicy, "issuedRemain", 0L);
            assertThat(couponPolicy.isQuantityAvailable()).isFalse();
        }
    }

    // ======= Test Stub 생성 메서드 =======

    private CouponPolicy createCouponPolicy(Long total, Long remain, LocalDateTime expiredDt) {
        CouponPolicy policy = new CouponPolicy();
        setField(policy, "issuedTotal", total);
        setField(policy, "issuedRemain", remain);
        setField(policy, "expiredDt", expiredDt);
        return policy;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}