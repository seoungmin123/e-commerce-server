package kr.hhplus.be.server.coupon.dto;

import kr.hhplus.be.server.coupon.domain.Coupon;

import java.time.LocalDateTime;

public record IssuedCouponResponseDto(
        Long couponId,
        String name,
        int discountRate,
        LocalDateTime issuedDt,
        LocalDateTime expiredDt,
        boolean isUsed
) {
    public static IssuedCouponResponseDto from(Coupon coupon) {
        return new IssuedCouponResponseDto(
                coupon.getCouponId(),
                coupon.getName(),
                coupon.getDiscountRate(),
                coupon.getIssuedDt(),
                coupon.getExpiredDt(),
                coupon.getIsUsed()
        );
    }
}