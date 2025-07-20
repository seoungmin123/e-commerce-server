package kr.hhplus.be.server.mock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.app.common.CommonResponse;
import kr.hhplus.be.server.mock.controller.dto.MockCouponDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Coupon", description = "쿠폰 관련 API")
public interface CouponControllerSpec {

    @Operation(summary = "선착순 쿠폰 발급", description = "요청한 쿠폰 정책에 대해 사용자가 쿠폰을 발급받는다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "쿠폰 발급 성공"),
            @ApiResponse(responseCode = "400", description = "발급 불가 (중복, 수량 초과 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "중복 발급 실패 예시",
                                    value = """
                    {
                      "success": false,
                      "message": "이미 발급된 쿠폰입니다.",
                      "data": null,
                      "status": 400
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "사용자 또는 쿠폰 정책 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "사용자 없음 실패 예시",
                                    value = """
                    {
                      "success": false,
                      "message": "사용자를 찾을 수 없습니다.",
                      "data": null,
                      "status": 404
                    }
                    """
                            )
                    )
            )
    })
    CommonResponse<MockCouponDto.IssuedCouponResponseDto> issueCoupon(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @RequestBody MockCouponDto.IssueCouponRequestDto request
    );

    @Operation(summary = "보유 쿠폰 목록 조회", description = "사용자가 보유한 전체 쿠폰 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "쿠폰 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "사용자 없음 실패 예시",
                                    value = """
                    {
                      "success": false,
                      "message": "사용자를 찾을 수 없습니다.",
                      "data": null,
                      "status": 404
                    }
                    """
                            )
                    )
            )
    })
    CommonResponse<List<MockCouponDto.IssuedCouponResponseDto>> getUserCoupons(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId
    );

    @Operation(summary = "쿠폰 사용 가능 여부 조회", description = "특정 사용자가 특정 쿠폰을 사용할 수 있는지 확인한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "쿠폰 사용 가능"),
            @ApiResponse(responseCode = "400", description = "쿠폰 사용 불가 (만료, 이미 사용됨 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "쿠폰 사용 불가 예시",
                                    value = """
                    {
                      "success": false,
                      "message": "이미 사용된 쿠폰입니다.",
                      "data": null,
                      "status": 400
                    }
                    """
                            )
                    )
            )
    })
    CommonResponse<MockCouponDto.CouponAvailableResponseDto> checkCouponAvailable(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "쿠폰 ID", required = true)
            @PathVariable Long couponId
    );
}
