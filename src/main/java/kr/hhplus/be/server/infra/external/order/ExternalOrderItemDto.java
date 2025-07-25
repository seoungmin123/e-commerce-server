package kr.hhplus.be.server.infra.external.order;

import kr.hhplus.be.server.order.domain.OrderItem;

public record ExternalOrderItemDto(
        Long productId,
        String productName,
        Long unitPrice,
        int quantity
) {
    public static ExternalOrderItemDto from(OrderItem item) {
        return new ExternalOrderItemDto(
                item.getProduct().getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity()
        );
    }
}