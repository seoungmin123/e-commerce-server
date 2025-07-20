package kr.hhplus.be.server.mock.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MockOrderDto {

    public record OrderRequestDto(
            Long userId,
            List<OrderItemRequestDto> items,
            Long couponId
    ) {}

    public record OrderItemRequestDto(
            Long productId,
            int item_cnt
    ) {}

    public record OrderSummaryResponseDto(
            Long orderId,
            OrderStatus status,
            int totalAmount
    ) {}

    public record UserOrderSummaryDto(
            Long orderId,
            LocalDateTime orderedDt,
            int totalPrice,
            OrderStatus status
    ) {}

    public record OrderDetailDto(
            Long orderId,
            LocalDateTime orderedDt,
            OrderStatus status,
            int totalPrice,
            Long couponId,
            List<OrderItemDto> items
    ) {}

    public record OrderItemDto(
            Long productId,
            int itemCnt,
            int unitPrice,
            int totalPrice
    ) {}

    public record OrderPaymentDto(
            Long orderPaymentId,
            Long couponId,
            int originalPrice,
            int discountAmount,
            int finalPrice,
            LocalDateTime paidDt
    ) {}

}
