package kr.hhplus.be.server.coupon.reader;

import kr.hhplus.be.server.coupon.domain.Coupon;

public interface CouponReader {
    boolean existsByUserIdAndCouponPolicyId(Long userId, Long couponPolicyId);

    Coupon findByIdAndUserId(Long couponId, Long userId);
    
}