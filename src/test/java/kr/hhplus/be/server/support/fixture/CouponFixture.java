package kr.hhplus.be.server.support.fixture;

import kr.hhplus.be.server.common.response.ApiResponseCode;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicyFixture;
import kr.hhplus.be.server.coupon.exception.CouponException;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.user.domain.User;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class CouponFixture {

    // 기본 쿠폰 생성 (ID 포함)
    public static Coupon withId(Long couponId) {
        CouponPolicy policy = CouponPolicyFixture.defaultPolicy();
        User user = UserFixture.createUser(1L, 10000L);
        return Coupon.ofTestValues(
                couponId,
                policy.getCouponPolicyId(),
                user.getUserId(),
                policy.getName(),
                policy.getDiscountRate(),
                false,
                LocalDateTime.now(),
                policy.getExpiredDt()
        );
    }

    // ID 없이 기본 쿠폰 생성
    public static Coupon defaultCoupon() {
        return withId(1L); // 기본 ID 1L로 사용
    }

    // 만료된 쿠폰 (테스트용)
    public static Coupon expiredCoupon() {
        CouponPolicy expiredPolicy = CouponPolicyFixture.withExpiredDate();
        User user = UserFixture.createUser(1L, 10000L);
        return Coupon.ofTestValues(
                2L,
                expiredPolicy.getCouponPolicyId(),
                user.getUserId(),
                expiredPolicy.getName(),
                expiredPolicy.getDiscountRate(),
                false,
                expiredPolicy.getExpiredDt().minusDays(1),
                expiredPolicy.getExpiredDt()
        );
    }

    // 이미 사용된 쿠폰
    public static Coupon usedCoupon() {
        Coupon coupon = defaultCoupon();
        coupon.markUsed();  // 내부에서 isUsed true, usedDt 설정됨
        return coupon;
    }

    // 예외 발생시키는 테스트용 쿠폰
    public static Coupon invalidCoupon() {
        Coupon coupon = mock(Coupon.class);
        when(coupon.getCouponId()).thenReturn(99L);

        doThrow(new TestCouponException(99L)).when(coupon).markUsed();

        return coupon;
    }

    // 테스트 전용 쿠폰 예외
    static class TestCouponException extends CouponException {
        public TestCouponException(Long couponId) {
            super(ApiResponseCode.FAIL_BAD_REQUEST_400, "테스트용 쿠폰 예외 발생. couponId=" + couponId);
        }
    }


}