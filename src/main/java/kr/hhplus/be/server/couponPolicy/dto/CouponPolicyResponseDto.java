package kr.hhplus.be.server.couponPolicy.dto;

import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;

import java.time.LocalDateTime;

public record CouponPolicyResponseDto(
        Long couponPolicyId,
        String name,
        int discountRate,
        Long issuedRemain,
        LocalDateTime expiredDt,
        boolean isExpired
) {
    public static CouponPolicyResponseDto from(CouponPolicy policy) {
        return new CouponPolicyResponseDto(
                policy.getCouponPolicyId(),
                policy.getName(),
                policy.getDiscountRate(),
                policy.getIssuedRemain(),
                policy.getExpiredDt(),
                policy.isExpired()
        );
    }
}