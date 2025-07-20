package kr.hhplus.be.server.mock.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.mock.controller.common.CommonResponse;
import kr.hhplus.be.server.mock.controller.dto.MockPointDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "포인트", description = "포인트 충전 및 사용 관련 API")
public interface PointControllerSpec {

    @Operation(
            summary = "포인트 잔액 조회",
            description = "사용자의 현재 보유 포인트를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "잔액 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "사용자 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "사용자 없음 예시",
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
            }
    )
    @GetMapping("/balance")
    CommonResponse<MockPointDto.PointBalanceResponseDto> getPointBalance(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId
    );

    @Operation(
            summary = "포인트 충전",
            description = "사용자의 포인트를 특정 금액만큼 충전합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MockPointDto.PointUpdateRequestDto.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                      "amount": 1000
                    }
                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "충전 성공"),
                    @ApiResponse(responseCode = "400", description = "0원 이하 충전 불가",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "0원 충전 실패 예시",
                                            value = """
                        {
                          "success": false,
                          "message": "충전 금액은 0원 이상이어야 합니다.",
                          "data": null,
                          "status": 400
                        }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "사용자 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "사용자 없음 예시",
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
            }
    )
    @PatchMapping("/charge")
    CommonResponse<MockPointDto.PointBalanceResponseDto> chargePoint(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @RequestBody MockPointDto.PointUpdateRequestDto request
    );

    @Operation(
            summary = "포인트 사용",
            description = "사용자의 포인트를 특정 금액만큼 차감합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = MockPointDto.PointUpdateRequestDto.class),
                            examples = @ExampleObject(
                                    value = """
                    {
                      "amount": 1000
                    }
                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "포인트 사용 성공"),
                    @ApiResponse(responseCode = "400", description = "포인트 부족",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "포인트 부족 예시",
                                            value = """
                        {
                          "success": false,
                          "message": "보유 포인트가 부족합니다.",
                          "data": null,
                          "status": 400
                        }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "사용자 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "사용자 없음 예시",
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
            }
    )
    @PatchMapping("/use")
    CommonResponse<MockPointDto.PointBalanceResponseDto> usePoint(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @RequestBody MockPointDto.PointUpdateRequestDto request
    );

    @Operation(
            summary = "포인트 사용 이력 조회",
            description = "사용자의 포인트 충전/사용 내역을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이력 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "사용자 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "사용자 없음 예시",
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
            }
    )
    @GetMapping("/history")
    CommonResponse<List<MockPointDto.PointHistoryDto>> getPointHistory(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId
    );
}
