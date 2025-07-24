package kr.hhplus.be.server.pointHistory.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.pointHistory.dto.PointHistoryResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "PointHistory API", description = "포인트 히스토리 API")
public interface PointHistorySwaggerDocs {

    @Operation(summary = "포인트 이력 조회", description = "사용자의 포인트 사용/충전 이력을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포인트 이력 조회 성공")
    })
    @GetMapping("/points/{userId}/history")
    kr.hhplus.be.server.common.response.ApiResponse<List<PointHistoryResponseDto>> getPointHistoryList(@PathVariable Long userId);
}