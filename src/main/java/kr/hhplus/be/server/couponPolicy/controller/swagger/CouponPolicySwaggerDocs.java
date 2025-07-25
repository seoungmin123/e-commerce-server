package kr.hhplus.be.server.couponPolicy.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.couponPolicy.dto.CouponPolicyResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Tag(name = "Coupon Policy API", description = "쿠폰 정책 관련 API")
public interface CouponPolicySwaggerDocs {

    @Operation(summary = "사용자 발급 가능한 쿠폰 정책 조회", description = "사용자가 현재 발급 가능한 쿠폰 정책 목록을 조회합니다.")
    @GetMapping("/available")
    kr.hhplus.be.server.common.response.ApiResponse<List<CouponPolicyResponseDto>> getAvailablePolicies(
            @Parameter(description = "사용자 ID", example = "1")
            @RequestParam Long userId);
}