package kr.hhplus.be.server.mock.controller.dto;
/**
 * 주문 상태 값
 */
public enum OrderStatus {
    CREATED,         // 주문 생성됨 (장바구니 완료 등)
    PAID,            // 결제 성공
    COMPLETED,       // 주문 완료 (배송까지 완료된 상태 등)
    PAYMENT_FAILED   // 결제 실패
}