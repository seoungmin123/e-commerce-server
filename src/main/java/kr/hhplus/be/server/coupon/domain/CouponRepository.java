package kr.hhplus.be.server.coupon.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long> {
    List<Coupon> findAllByUserId(Long userId);
    boolean existsByUserIdAndCouponPolicyId(Long userId, Long couponPolicyId);
    Coupon findByCouponIdAndUserId(Long couponId, Long userId);

}