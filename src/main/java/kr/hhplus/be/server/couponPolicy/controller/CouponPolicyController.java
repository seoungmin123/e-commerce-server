package kr.hhplus.be.server.couponPolicy.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ApiResponseCode;
import kr.hhplus.be.server.couponPolicy.controller.swagger.CouponPolicySwaggerDocs;
import kr.hhplus.be.server.couponPolicy.dto.CouponPolicyResponseDto;
import kr.hhplus.be.server.couponPolicy.facade.CouponPolicyFacade;
import kr.hhplus.be.server.couponPolicy.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupon-policy")
@RequiredArgsConstructor
public class CouponPolicyController implements CouponPolicySwaggerDocs {

    private final CouponPolicyService couponPolicyService;
    private final CouponPolicyFacade couponPolicyFacade;

    // 특정사용자가 발급 가능한 쿠폰정책 조회
    @GetMapping("/available")
    public ApiResponse<List<CouponPolicyResponseDto>> getAvailablePolicies(@RequestParam Long userId) {
        List<CouponPolicyResponseDto> result = couponPolicyFacade.getAvailablePolicies(userId);
        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, result);
    }

}