package kr.hhplus.be.server.support.fixture;

import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderItem;
import kr.hhplus.be.server.order.domain.OrderPayment;
import kr.hhplus.be.server.order.domain.OrderStatus;
import kr.hhplus.be.server.order.dto.OrderItemRequestDto;
import kr.hhplus.be.server.order.dto.OrderRequestDto;
import kr.hhplus.be.server.product.domain.Product;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderFixture {

    public static OrderRequestDto 주문요청_쿠폰없음() {
        return new OrderRequestDto(
                1L,
                List.of(new OrderItemRequestDto(1L, 2, 5000L)),
                null
        );
    }

    public static OrderRequestDto 주문요청_쿠폰있음() {
        return new OrderRequestDto(
                1L,
                List.of(new OrderItemRequestDto(1L, 2, 5000L)),
                3L // 실제 DB에 존재해야 하는 쿠폰 ID
        );
    }

    public static OrderRequestDto createOrderRequestWithCoupon() {
        return new OrderRequestDto(
                1L,
                List.of(new OrderItemRequestDto(1L, 2, 1000L)),
                10L  // couponId
        );
    }

    public static OrderRequestDto createOrderRequestWithoutCoupon() {
        return new OrderRequestDto(
                1L,
                List.of(new OrderItemRequestDto(1L, 1, 1000L)),
                null  // no coupon
        );
    }

    public static Order defaultOrder() {
        // 이건 도메인 mock용 (단위 테스트에서 사용)
        Order order = mock(Order.class);
        when(order.getOrderId()).thenReturn(1L);
        return order;
    }

    public static Order defaultOrderWithAllFields() {
        // Product Mock
        Product product = mock(Product.class);
        when(product.getProductId()).thenReturn(1L);
        when(product.getName()).thenReturn("테스트상품");

        // OrderItem Mock
        OrderItem item = mock(OrderItem.class);
        when(item.getProduct()).thenReturn(product);
        when(item.getProductName()).thenReturn("테스트상품");
        when(item.getQuantity()).thenReturn(2);
        when(item.getTotalPrice()).thenReturn(2000L);

        // Payment Mock
        OrderPayment payment = mock(OrderPayment.class);
        when(payment.getOriginalPrice()).thenReturn(2000L);
        when(payment.getDiscountAmount()).thenReturn(500L);
        when(payment.getFinalPrice()).thenReturn(1500L);

        // Order Mock
        Order order = mock(Order.class);
        when(order.getOrderId()).thenReturn(1L);
        when(order.getStatus()).thenReturn(OrderStatus.COMPLETED);
        when(order.getOrderedDt()).thenReturn(LocalDateTime.of(2025, 7, 24, 10, 0));
        when(order.getOrderPayment()).thenReturn(payment);
        when(order.getOrderItems()).thenReturn(List.of(item));

        return order;
    }


}