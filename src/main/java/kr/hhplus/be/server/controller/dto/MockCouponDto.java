package kr.hhplus.be.server.controller.dto;

import java.time.LocalDateTime;

public class MockCouponDto {

    public record IssueCouponRequestDto(Long couponPolicyId) {}

    public record IssuedCouponResponseDto(
            Long couponId,
            Long userId,
            Long couponPolicyId,
            String name,
            int discountRate,
            LocalDateTime issuedAt,
            LocalDateTime expiredAt,
            boolean isUsed
    ) {}

    public record CouponAvailableResponseDto(
            boolean available,
            CouponReason reason,
            IssuedCouponResponseDto coupon
    ) {}
}