package kr.hhplus.be.server.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.order.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
@Schema(description = "주문 상세 조회 응답 DTO")
public record OrderDetailResponseDto(
        Long orderId,
        Long userId,
        LocalDateTime orderedDt,
        List<OrderItemDetailDto> orderItems,
        Long totalAmount,
        String status,
        OrderPaymentResponseDto paymentInfo
) {
    public static OrderDetailResponseDto from(Order order) {
    return new OrderDetailResponseDto(
            order.getOrderId(),
            order.getUser().getUserId(),
            order.getOrderedDt(),
            order.getOrderItems().stream()
                    .map(OrderItemDetailDto::from)
                    .toList(),
            order.getTotalPrice(),
            order.getStatus().name(),
            OrderPaymentResponseDto.from(order.getOrderPayment())
    );
}}