package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderItem;
import kr.hhplus.be.server.order.domain.OrderPayment;
import kr.hhplus.be.server.order.domain.OrderStatus;
import kr.hhplus.be.server.order.dto.OrderDetailResponseDto;
import kr.hhplus.be.server.order.dto.OrderPaymentResponseDto;
import kr.hhplus.be.server.order.dto.OrderSummaryResponseDto;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import kr.hhplus.be.server.order.exception.OrderPaymentNotFoundException;
import kr.hhplus.be.server.order.repository.OrderPaymentRepository;
import kr.hhplus.be.server.order.repository.OrderRepository;
import kr.hhplus.be.server.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderPaymentRepository orderPaymentRepository;

    @InjectMocks
    OrderService orderService;

    public OrderServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("createOrder")
    class CreateOrderTest {
        @Test
        @DisplayName("주문 생성에 성공한다")
        void success() {
            // given
            User user = mock(User.class);
            OrderItem item = mock(OrderItem.class);
            List<OrderItem> items = List.of(item);
            CouponPolicy policy = mock(CouponPolicy.class);

            OrderPayment payment = OrderPayment.create(10000L, policy);
            Order order = Order.create(user, items, payment);

            when(orderRepository.save(any(Order.class))).thenReturn(order);

            // when
            Order result = orderService.createOrder(user, items, 10000L, policy);
            result.markAsCompleted(); //주문완료

            // then
            assertThat(result.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        }
    }

    @Nested
    @DisplayName("getOrderListByUserId")
    class GetOrderListByUserIdTest {
        @Test
        @DisplayName("유저 ID로 주문 목록을 조회한다")
        void success() {
            // given
            Order order = mock(Order.class);
            when(orderRepository.findAllByUserUserId(1L)).thenReturn(List.of(order));

            // when
            List<OrderSummaryResponseDto> result = orderService.getOrderListByUserId(1L);

            // then
            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getOrderDetail")
    class GetOrderDetailTest {
        @Test
        @DisplayName("주문 상세를 정상 조회한다")
        void success() {
            // given
            Order order = mock(Order.class);
            User user = mock(User.class);

            when(user.getUserId()).thenReturn(1L);
            when(order.getUser()).thenReturn(user);
            when(order.getOrderItems()).thenReturn(List.of()); // items도 비워주면 NPE 방지
            when(order.getOrderPayment()).thenReturn(mock(OrderPayment.class));
            when(order.getStatus()).thenReturn(OrderStatus.COMPLETED);
            when(order.getOrderedDt()).thenReturn(LocalDateTime.now());

            when(orderRepository.findByIdWithOrderItems(1L)).thenReturn(Optional.of(order));

            // when
            OrderDetailResponseDto result = orderService.getOrderDetail(1L);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("주문 ID가 존재하지 않으면 예외 발생")
        void fail_when_not_found() {
            when(orderRepository.findByIdWithOrderItems(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.getOrderDetail(1L))
                    .isInstanceOf(OrderNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getOrderPayment")
    class GetOrderPaymentTest {
        @Test
        @DisplayName("주문 결제 정보를 정상 조회한다")
        void success() {
            OrderPayment payment = mock(OrderPayment.class);
            when(orderPaymentRepository.findByOrderOrderId(1L)).thenReturn(Optional.of(payment));

            OrderPaymentResponseDto result = orderService.getOrderPayment(1L);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("결제 정보가 존재하지 않으면 예외 발생")
        void fail_when_not_found() {
            when(orderPaymentRepository.findByOrderOrderId(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.getOrderPayment(1L))
                    .isInstanceOf(OrderPaymentNotFoundException.class);
        }
    }
}
