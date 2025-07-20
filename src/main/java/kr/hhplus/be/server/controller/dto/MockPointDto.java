package kr.hhplus.be.server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MockPointDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointBalanceResponseDto {
        private Long userId;
        private int point;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointUpdateRequestDto {
        private int amount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointHistoryDto {
        private Long pointHistoryId;
        private int amount;
        private PointType type;  // CHARGE or USE
        private LocalDateTime createdDt;
        private int balanceAfter;
    }
}
