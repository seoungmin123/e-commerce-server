package kr.hhplus.be.server.coupon.facade;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.dto.IssuedCouponResponseDto;
import kr.hhplus.be.server.coupon.exception.AlreadyIssuedCouponException;
import kr.hhplus.be.server.coupon.reader.CouponReader;
import kr.hhplus.be.server.coupon.domain.CouponRepository;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.couponPolicy.reader.CouponPolicyReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponIssueFacade {

    private final CouponPolicyReader couponPolicyReader;
    private final CouponReader couponReader;
    private final CouponRepository couponRepository;

    /**
     * 선착순 쿠폰 발급 (TODO : 동시성)
     * */
    @Transactional
    public IssuedCouponResponseDto issue(Long userId, Long couponPolicyId) {
        //쿠폰 정책 존재 확인
        CouponPolicy policy = couponPolicyReader.findByIdOrThrow(couponPolicyId);

        policy.validateIssueAvailability(); //쿠폰 발급 가능 수량 검증

        //중복발급 여부확인
        if (couponReader.existsByUserIdAndCouponPolicyId(userId, couponPolicyId)) {
            throw new AlreadyIssuedCouponException(userId, couponPolicyId);
        }

        policy.decreaseIssuedRemain(); // 수량 차감

        //쿠폰생성
        Coupon coupon = Coupon.issue(policy, userId);

        //쿠폰저장
        //TODO : 질문 writer를 따로 분리해야하나
        return IssuedCouponResponseDto.from(couponRepository.save(coupon));
    }
}