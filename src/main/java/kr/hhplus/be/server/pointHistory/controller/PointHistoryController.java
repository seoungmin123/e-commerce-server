package kr.hhplus.be.server.pointHistory.controller;

import kr.hhplus.be.server.common.response.ApiResponse;
import kr.hhplus.be.server.common.response.ApiResponseCode;
import kr.hhplus.be.server.pointHistory.controller.swagger.PointHistorySwaggerDocs;
import kr.hhplus.be.server.pointHistory.dto.PointHistoryResponseDto;
import kr.hhplus.be.server.pointHistory.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointHistoryController implements PointHistorySwaggerDocs {

    private final PointService pointService;

    // 포인트 이력 조회 로직
    @GetMapping("/{userId}/history")
    public ApiResponse<List<PointHistoryResponseDto>> getPointHistoryList (@PathVariable Long userId) {
        List<PointHistoryResponseDto> pointHistories = pointService.getPointHistory(userId);
        return ApiResponse.success(ApiResponseCode.SUCCESS_OK_200, pointHistories);
    }
}