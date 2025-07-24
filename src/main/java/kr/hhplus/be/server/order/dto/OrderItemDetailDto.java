package kr.hhplus.be.server.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.order.domain.OrderItem;

@Schema(description = "주문 아이템 상세 응답 DTO")
public record OrderItemDetailDto (
        String name,
        long price,
        long quantity,
        long totalPrice
){
    public static OrderItemDetailDto from(OrderItem item) {
        return new OrderItemDetailDto(
                item.getProductName(),         // item.getProduct().getName() 같은 식
                item.getUnitPrice(),
                item.getQuantity(),
                item.getTotalPrice()
        );
    }
}