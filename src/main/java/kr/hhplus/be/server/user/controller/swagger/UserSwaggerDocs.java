package kr.hhplus.be.server.user.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.user.dto.PointBalanceDto;
import kr.hhplus.be.server.user.dto.PointChargeRequestDto;
import kr.hhplus.be.server.user.dto.PointUseRequestDto;
import kr.hhplus.be.server.user.dto.UserResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User API", description = "User API")
public interface UserSwaggerDocs {

    @Operation(summary = "특정 사용자 정보 조회", description = "userId를 이용하여 단일 사용자 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "정상 조회 완료",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))
            )
    })
    @GetMapping("/{userId}")
    ApiResponse<UserResponseDto> getUserInfo(@PathVariable Long userId);

    @Operation(summary = "사용자 포인트 잔액 조회", description = "사용자의 포인트 잔액을 반환합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "포인트 잔액 조회 성공",
                    content = @Content(schema = @Schema(implementation = PointBalanceDto.class))
            )
    })
    @GetMapping("/{userId}/point")
    ApiResponse<PointBalanceDto> getPoint(@PathVariable Long userId);

    @Operation(summary = "포인트 충전", description = "요청된 금액만큼 사용자의 포인트를 충전합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "충전 성공",
                    content = @Content(schema = @Schema(implementation = PointBalanceDto.class))
            )
    })
    @PostMapping("/{userId}/charge")
    ApiResponse<PointBalanceDto> chargePoint(@PathVariable Long userId, @RequestBody PointChargeRequestDto requestDto);

    @Operation(summary = "포인트 사용", description = "요청된 금액만큼 사용자의 포인트를 차감합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "차감 성공",
                    content = @Content(schema = @Schema(implementation = PointBalanceDto.class))
            )
    })
    @PostMapping("/{userId}/use")
    ApiResponse<PointBalanceDto> usePoint(@PathVariable Long userId, @RequestBody PointUseRequestDto requestDto);
}