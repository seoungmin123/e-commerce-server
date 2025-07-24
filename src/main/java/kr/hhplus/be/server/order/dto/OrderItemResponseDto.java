package kr.hhplus.be.server.order.dto;

import kr.hhplus.be.server.order.domain.OrderItem;

public record OrderItemResponseDto(
        Long productId,
        int quantity,
        Long unitPrice,
        Long totalPrice
) {
    public static OrderItemResponseDto from(OrderItem item) {
        return new OrderItemResponseDto(
                item.getProduct().getProductId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }
}