package kr.hhplus.be.server.coupon.reader;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponRepository;
import kr.hhplus.be.server.coupon.exception.CouponNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaCouponReader implements CouponReader {

    private final CouponRepository couponRepository;

    @Override
    public boolean existsByUserIdAndCouponPolicyId(Long userId, Long couponPolicyId) {
        return couponRepository.existsByUserIdAndCouponPolicyId(userId, couponPolicyId);
    }

    @Override
    public Coupon findByIdAndUserId(Long userId, Long couponId) {
        Coupon coupon = couponRepository.findByCouponIdAndUserId(couponId, userId);
        if (coupon == null) {
            throw new CouponNotFoundException(userId, couponId);
        }
        return coupon;
    }
}