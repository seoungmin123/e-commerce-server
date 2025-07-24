package kr.hhplus.be.server.order.dto;

import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderPayment;
import kr.hhplus.be.server.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreatedResponseDto(
        Long orderId,
        OrderStatus status,
        Long originalPrice,
        Long discountAmount,
        Long finalPrice,
        LocalDateTime orderedDt,
        List<OrderItemResponseDto> items
) {
    public static OrderCreatedResponseDto from(Order order) {
        OrderPayment payment = order.getOrderPayment();

        return new OrderCreatedResponseDto(
                order.getOrderId(),
                order.getStatus(),
                payment.getOriginalPrice(),
                payment.getDiscountAmount(),
                payment.getFinalPrice(),
                order.getOrderedDt(),
                order.getOrderItems().stream()
                        .map(OrderItemResponseDto::from)
                        .toList()
        );
    }
}