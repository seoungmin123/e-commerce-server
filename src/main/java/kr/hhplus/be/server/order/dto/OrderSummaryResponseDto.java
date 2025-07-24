package kr.hhplus.be.server.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderStatus;

import java.time.LocalDateTime;

@Schema(description = "주문 전체 조회 정보 응답 DTO")
public record OrderSummaryResponseDto(
        Long orderId,
        OrderStatus status,
        LocalDateTime orderedDt,
        Long totalPrice
) {
    public static OrderSummaryResponseDto from(Order order) {
        return new OrderSummaryResponseDto(
                order.getOrderId(),
                order.getStatus(),
                order.getOrderedDt(),
                order.getTotalPrice()
        );
    }
}