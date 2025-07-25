package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.dto.IssuedCouponResponseDto;
import kr.hhplus.be.server.coupon.domain.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    // 사용자가 발급받은 쿠폰 목록조회
    @Transactional(readOnly = true)
    public List<IssuedCouponResponseDto> getUserCoupons(Long userId) {
        //없으면 빈리스트 반환
        List<Coupon> Coupons = couponRepository.findAllByUserId(userId);
        if (Coupons == null) {
            return Collections.emptyList();
        }

        return Coupons.stream()
                .map(IssuedCouponResponseDto::from)
                .toList();
    }

}