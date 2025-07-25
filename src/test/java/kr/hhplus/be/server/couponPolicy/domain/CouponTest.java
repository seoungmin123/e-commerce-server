package kr.hhplus.be.server.couponPolicy.domain;

import kr.hhplus.be.server.coupon.domain.Coupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
@DisplayName("Coupon 도메인 테스트")
class CouponTest {

    @Nested
    @DisplayName("issue 메서드")
    class Issue {

        @Test
        @DisplayName("정책 기반으로 Coupon 객체를 정상 생성한다")
        void issue_메서드는_Coupon객체를_정상_생성한다() {
            // given
            CouponPolicy policy = createCouponPolicyStub(1L, "10% 할인", 10, LocalDateTime.now().plusDays(3));
            Long userId = 123L;

            // when
            Coupon coupon = Coupon.issue(policy, userId);

            // then
            assertThat(coupon.getUserId()).isEqualTo(userId);
            assertThat(coupon.getCouponPolicyId()).isEqualTo(policy.getCouponPolicyId());
            assertThat(coupon.getName()).isEqualTo(policy.getName());
            assertThat(coupon.getDiscountRate()).isEqualTo(policy.getDiscountRate());
            assertThat(coupon.getExpiredDt()).isEqualTo(policy.getExpiredDt());
            assertThat(coupon.getIsUsed()).isFalse();
            assertThat(coupon.getIssuedDt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("isExpired 메서드")
    class IsExpired {

        @Test
        @DisplayName("만료 시간이 지나면 true를 반환한다")
        void isExpired_만료시간이_지났으면_true() {
            Coupon coupon = createCouponStub(LocalDateTime.now().minusSeconds(1));
            assertThat(coupon.isExpired()).isTrue();
        }

        @Test
        @DisplayName("아직 유효하면 false를 반환한다")
        void isExpired_아직_유효하면_false() {
            Coupon coupon = createCouponStub(LocalDateTime.now().plusDays(1));
            assertThat(coupon.isExpired()).isFalse();
        }
    }

    @Nested
    @DisplayName("isUsable 메서드")
    class IsUsable {

        @Test
        @DisplayName("미사용 + 만료되지 않으면 true")
        void isUsable_미사용_만료되지않음_true() {
            Coupon coupon = createCouponStub(LocalDateTime.now().plusDays(1));
            assertThat(coupon.isUsable()).isTrue();
        }

        @Test
        @DisplayName("사용된 쿠폰은 false")
        void isUsable_사용됨_false() {
            Coupon coupon = createCouponStub(LocalDateTime.now().plusDays(1));
            coupon.markUsed();
            assertThat(coupon.isUsable()).isFalse();
        }
    }

    @Nested
    @DisplayName("markUsed 메서드")
    class MarkUsed {

        @Test
        @DisplayName("정상 호출 시 쿠폰이 사용 처리된다")
        void markUsed_정상동작() {
            Coupon coupon = createCouponStub(LocalDateTime.now().plusDays(1));

            coupon.markUsed();

            assertThat(coupon.getIsUsed()).isTrue();
            assertThat(coupon.getUsedDt()).isNotNull();
        }

        @Test
        @DisplayName("이미 사용된 쿠폰은 예외 발생")
        void markUsed_이미사용된_쿠폰이면_예외발생() {
            Coupon coupon = createCouponStub(LocalDateTime.now().plusDays(1));
            coupon.markUsed();

            assertThatThrownBy(coupon::markUsed)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("이미 사용된 쿠폰입니다.");
        }

        @Test
        @DisplayName("만료된 쿠폰은 예외 발생")
        void markUsed_만료된_쿠폰이면_예외발생() {
            Coupon coupon = createCouponStub(LocalDateTime.now().minusDays(1));

            assertThatThrownBy(coupon::markUsed)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("이미 만료된 쿠폰입니다.");
        }
    }

    // ======= Test Stub 생성 메서드 =======

    private Coupon createCouponStub(LocalDateTime expiredDt) {
        return new Coupon(1L, 123L, "10% 할인", 10, expiredDt);
    }

    private CouponPolicy createCouponPolicyStub(Long id, String name, int discountRate, LocalDateTime expiredDt) {
        CouponPolicy policy = new CouponPolicy();
        setField(policy, "couponPolicyId", id);
        setField(policy, "name", name);
        setField(policy, "discountRate", discountRate);
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