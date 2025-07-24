package kr.hhplus.be.server.pointHistory.dto;

import kr.hhplus.be.server.pointHistory.domain.PointHistory;
import kr.hhplus.be.server.pointHistory.domain.PointType;

import java.time.LocalDateTime;

public record PointHistoryResponseDto(
        Long pointHistoryId,
        Long amount,
        PointType type,
        LocalDateTime createdDt,
        Long balanceAfter
) {
    public static PointHistoryResponseDto from(PointHistory pointHistory) {
       return new PointHistoryResponseDto(
                        pointHistory.getPointHistoryId(),
                        pointHistory.getAmount(),
                        pointHistory.getType(),
                        pointHistory.getCreatedDt(),
                        pointHistory.getBalanceAfter()
       );
    }
}