package kr.hhplus.be.server.couponPolicy.facade;

import kr.hhplus.be.server.coupon.reader.CouponReader;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.couponPolicy.dto.CouponPolicyResponseDto;
import kr.hhplus.be.server.couponPolicy.reader.CouponPolicyReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponPolicyFacade {

    private final CouponPolicyReader couponPolicyReader;
    private final CouponReader couponReader;

    // 특정사용자가 발급 가능한 쿠폰정책 조회
    public List<CouponPolicyResponseDto> getAvailablePolicies(Long userId) {
        //없을시 빈 리스트 반환
        List<CouponPolicy> policies = couponPolicyReader.findAll();
        if (policies == null) {
            return Collections.emptyList();
        }

        return policies.stream()
                .filter(policy -> !policy.isExpired())
                .filter(CouponPolicy::isQuantityAvailable)
                .filter(policy -> !couponReader.existsByUserIdAndCouponPolicyId(userId, policy.getCouponPolicyId()))
                .map(CouponPolicyResponseDto::from)
                .toList();
    }
}