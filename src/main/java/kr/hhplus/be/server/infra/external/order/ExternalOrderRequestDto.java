package kr.hhplus.be.server.infra.external.order;

import kr.hhplus.be.server.order.domain.Order;

import java.util.List;

public record ExternalOrderRequestDto(
        Long orderId,
        Long userId,
        Long totalPrice,
        List<ExternalOrderItemDto> items
) {

    public static ExternalOrderRequestDto from(Order order) {
        return new ExternalOrderRequestDto(
                order.getOrderId(),
                order.getUser().getUserId(),
                order.getTotalPrice(),
                order.getOrderItems().stream()
                        .map(ExternalOrderItemDto::from)
                        .toList()
        );
    }
}
