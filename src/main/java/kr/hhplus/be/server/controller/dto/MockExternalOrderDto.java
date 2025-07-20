package kr.hhplus.be.server.controller.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MockExternalOrderDto {

    public record ExternalOrderRequestDto(
            Long orderId,
            Long userId,
            int totalAmount,
            String status, // "SUCCESS" or "FAIL"
            String reason, // 실패 시 사유
            List<MockOrderDto.OrderItemRequestDto> items
    ) {}

    public record ExternalOrderItemDto(
            Long productId,
            int item_cnt
    ) {}
}
