package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.common.ResponseApi;
import kr.hhplus.be.server.controller.dto.MockPointDto.PointBalanceResponseDto;
import kr.hhplus.be.server.controller.dto.MockPointDto.PointHistoryDto;
import kr.hhplus.be.server.controller.dto.MockPointDto.PointUpdateRequestDto;
import kr.hhplus.be.server.controller.dto.PointType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mock/users/{userId}/points")
public class PointController implements PointControllerSpec {

    /** 포인트 잔액조회 */
    @GetMapping("/balance")
    public ResponseApi<PointBalanceResponseDto> getPointBalance(@PathVariable Long userId) {
        return ResponseApi.success("성공", new PointBalanceResponseDto(userId, 15000), 200);
    }

    /** 포인트 충전 */
    @PatchMapping("/charge")
    public ResponseApi<PointBalanceResponseDto> chargePoint(
            @PathVariable Long userId,
            @RequestBody PointUpdateRequestDto request
    ) {
        return ResponseApi.success("포인트 충전 완료", new PointBalanceResponseDto(userId, 20000), 200);
    }

    /** 포인트 사용 */
    @PatchMapping("/use")
    public ResponseApi<PointBalanceResponseDto> usePoint(
            @PathVariable Long userId,
            @RequestBody PointUpdateRequestDto request
    ) {
        return ResponseApi.success("포인트 사용 완료", new PointBalanceResponseDto(userId, 5000), 200);
    }

    /** 포인트 히스토리 조회*/
    @GetMapping("/history")
    public ResponseApi<List<PointHistoryDto>> getPointHistory(@PathVariable Long userId) {
        return ResponseApi.success("성공", List.of(
                new PointHistoryDto(1L, 1000, PointType.CHARGE, LocalDateTime.of(2025, 7, 17, 15, 30), 10000),
                new PointHistoryDto(2L, -500, PointType.USE, LocalDateTime.of(2025, 7, 17, 18, 0), 9500)
        ), 200);
    }
}
