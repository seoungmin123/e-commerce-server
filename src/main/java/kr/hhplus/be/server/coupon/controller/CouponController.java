package kr.hhplus.be.server.coupon.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ApiResponseCode;
import kr.hhplus.be.server.coupon.controller.swagger.CouponSwaggerDocs;
import kr.hhplus.be.server.coupon.dto.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.dto.IssuedCouponResponseDto;
import kr.hhplus.be.server.coupon.facade.CouponIssueFacade;
import kr.hhplus.be.server.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController implements CouponSwaggerDocs {

    private final CouponService couponService;
    private final CouponIssueFacade couponIssueFacade;


    // 쿠폰 발급 로직 구현 후 반환

    @PostMapping("/{userId}")
    public ApiResponse<IssuedCouponResponseDto> issueCoupon(
            @PathVariable Long userId,
            @RequestBody CouponIssueRequestDto request
    ) {
        IssuedCouponResponseDto coupon = couponIssueFacade.issue(userId, request.couponPolicyId());
        return ApiResponse.success(ApiResponseCode.SUCCESS_CREATED_201, coupon);
    }

    // 보유 쿠폰 목록 조회 로직 구현 후 반환
    @GetMapping("/{userId}")
    public ApiResponse<List<IssuedCouponResponseDto>> getUserCoupons(@PathVariable Long userId) {
       List<IssuedCouponResponseDto> coupons= couponService.getUserCoupons(userId);

       return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, coupons);
    }

}