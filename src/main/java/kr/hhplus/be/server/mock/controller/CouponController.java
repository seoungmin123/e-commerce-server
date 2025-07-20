package kr.hhplus.be.server.mock.controller;


import kr.hhplus.be.server.app.common.CommonResponse;
import kr.hhplus.be.server.mock.controller.dto.MockCouponDto;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mock/users/{userId}/coupons")
public class CouponController implements CouponControllerSpec{

    /** 선착순 쿠폰 발급 */
    @PostMapping
    public CommonResponse<MockCouponDto.IssuedCouponResponseDto> issueCoupon(
            @PathVariable Long userId,
            @RequestBody MockCouponDto.IssueCouponRequestDto request
    ) {
        return CommonResponse.success("쿠폰 발급 완료", new MockCouponDto.IssuedCouponResponseDto(
                5L, userId, request.couponPolicyId(), "10% 할인 쿠폰", 10,
                LocalDateTime.of(2025, 7, 17, 16, 34),
                LocalDateTime.of(2025, 7, 24, 23, 59),
                false
        ), 200);
    }

    /** 사용자별 쿠폰 보유 목록 */
    @GetMapping
    public CommonResponse<List<MockCouponDto.IssuedCouponResponseDto>> getUserCoupons(@PathVariable Long userId) {
        return CommonResponse.success("성공", List.of(
                new MockCouponDto.IssuedCouponResponseDto(
                        5L, userId, 1L, "10% 할인 쿠폰", 10,
                        LocalDateTime.of(2025, 7, 17, 12, 0),
                        LocalDateTime.of(2025, 7, 31, 23, 59),
                        false
                )
        ), 200);
    }

    /** 쿠폰 사용가능 여부 */
    @GetMapping("/{couponId}/available")
    public CommonResponse<MockCouponDto.CouponAvailableResponseDto> checkCouponAvailable(
            @PathVariable Long userId,
            @PathVariable Long couponId
    ) {
        return CommonResponse.success("성공", new MockCouponDto.CouponAvailableResponseDto(
                true,
                null,
                new MockCouponDto.IssuedCouponResponseDto(
                        couponId, userId, 1L, "10% 할인 쿠폰", 10,
                        LocalDateTime.of(2025, 7, 17, 12, 0),
                        LocalDateTime.of(2025, 7, 31, 23, 59),
                        false
                )
        ), 200);
    }
}
