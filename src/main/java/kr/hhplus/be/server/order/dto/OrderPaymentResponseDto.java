package kr.hhplus.be.server.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.order.domain.OrderPayment;

import java.time.LocalDateTime;

@Schema(description = "주문 결제 정보 응답 DTO")
public record OrderPaymentResponseDto(
        Long orderPaymentId,
        Long couponId,
        Long originalPrice,
        Long discountAmount,
        Long finalPrice,
        LocalDateTime paidDt
) {
    public static OrderPaymentResponseDto from(OrderPayment payment) {
        return new OrderPaymentResponseDto(
                payment.getOrderPaymentId(),
                payment.getCouponId(),
                payment.getOriginalPrice(),
                payment.getDiscountAmount(),
                payment.getFinalPrice(),
                payment.getPaidDt()
        );
    }
}