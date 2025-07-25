package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderItem;
import kr.hhplus.be.server.order.domain.OrderPayment;
import kr.hhplus.be.server.order.dto.OrderDetailResponseDto;
import kr.hhplus.be.server.order.dto.OrderPaymentResponseDto;
import kr.hhplus.be.server.order.dto.OrderSummaryResponseDto;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import kr.hhplus.be.server.order.exception.OrderPaymentNotFoundException;
import kr.hhplus.be.server.order.repository.OrderPaymentRepository;
import kr.hhplus.be.server.order.repository.OrderRepository;
import kr.hhplus.be.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    // 주문 생성 처리
    @Transactional
    public Order createOrder(
            User user,
            List<OrderItem> orderItems,
            Long originalPrice,
            CouponPolicy policy
    ) {
        // 결제 도메인 생성 (쿠폰 적용 포함)
        OrderPayment payment = OrderPayment.create(originalPrice, policy);

        // 주문 도메인 생성 (연관관계 설정)
        Order order = Order.create(user, orderItems, payment);

        // 주문 상태값 변경
        order.markAsCompleted();

        // 저장 반환
        return orderRepository.save(order);
    }

    // 주문전체조회
    @Transactional(readOnly = true)
    public List<OrderSummaryResponseDto> getOrderListByUserId(Long userId) {
        return orderRepository.findAllByUserUserId(userId).stream()
                .map(OrderSummaryResponseDto::from)
                .toList();
    }

    // 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderDetail(Long orderId) {
        Order order = orderRepository.findByIdWithOrderItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return OrderDetailResponseDto.from(order);
    }

    // 결제내역조회
    @Transactional(readOnly = true)
    public OrderPaymentResponseDto getOrderPayment(Long orderId) {
        OrderPayment payment = orderPaymentRepository.findByOrderOrderId(orderId)
                .orElseThrow(() -> new OrderPaymentNotFoundException(orderId));

        return OrderPaymentResponseDto.from(payment);
    }


}