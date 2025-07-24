package kr.hhplus.be.server.couponPolicy.domain;

import java.time.LocalDateTime;

// CouponPolicyFixture.java
public class CouponPolicyFixture {

    public static CouponPolicy defaultPolicy() {
        return CouponPolicy.ofTestValues(
                1L,
                "기본 할인쿠폰",
                10,
                100L,
                100L,
                LocalDateTime.now().plusDays(7)
        );
    }

    public static CouponPolicy withExpiredDate() {
        return CouponPolicy.ofTestValues(
                99L,
                "만료된 쿠폰",
                10,
                100L,
                100L,
                LocalDateTime.now().minusDays(1)
        );
    }
}