package kr.hhplus.be.server.coupon.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.coupon.dto.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.dto.IssuedCouponResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// CouponController Swagger 인터페이스
@Tag(name = "Coupon API", description = "쿠폰 발급 및 조회 API")
public interface CouponSwaggerDocs {

    @Operation(summary = "쿠폰 발급", description = "사용자에게 쿠폰을 발급합니다.")
    @PostMapping("/{userId}")
    kr.hhplus.be.server.common.response.ApiResponse<IssuedCouponResponseDto> issueCoupon(
            @Parameter(description = "사용자 ID") @PathVariable Long userId,
            @RequestBody CouponIssueRequestDto request);

    @Operation(summary = "사용자 쿠폰 목록 조회", description = "특정 사용자의 보유 쿠폰을 조회합니다.")
    @GetMapping("/{userId}")
    kr.hhplus.be.server.common.response.ApiResponse<List<IssuedCouponResponseDto>> getUserCoupons(
            @Parameter(description = "사용자 ID") @PathVariable Long userId);
}