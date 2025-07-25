package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicyFixture;
import kr.hhplus.be.server.coupon.reader.CouponReader;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.couponPolicy.reader.CouponPolicyReader;
import kr.hhplus.be.server.infra.external.order.ExternalOrderRequestDto;
import kr.hhplus.be.server.infra.external.order.ExternalOrderSender;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.dto.OrderCreatedResponseDto;
import kr.hhplus.be.server.order.dto.OrderRequestDto;
import kr.hhplus.be.server.order.dto.ProductSalesDto;
import kr.hhplus.be.server.order.repository.OrderItemRepository;
import kr.hhplus.be.server.order.service.OrderService;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductFixture;
import kr.hhplus.be.server.product.dto.PopularProductResponseDto;
import kr.hhplus.be.server.product.reader.ProductReader;
import kr.hhplus.be.server.support.fixture.CouponFixture;
import kr.hhplus.be.server.support.fixture.OrderFixture;
import kr.hhplus.be.server.support.fixture.UserFixture;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.reader.UserReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderFacadeTest {

    @InjectMocks
    private OrderFacade orderFacade;

    @Mock private ProductReader productReader;
    @Mock private CouponReader couponReader;
    @Mock private CouponPolicyReader couponPolicyReader;
    @Mock
    private OrderService orderService;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private UserReader userReader;
    @Mock private ExternalOrderSender externalOrderSender;

    @Nested
    @DisplayName("주문 생성(createOrder) -")
    class CreateOrder {

        @Test
        @DisplayName("성공: 쿠폰 적용 및 포인트 차감 포함")
        void success_create_order_with_coupon() {
            // given
            OrderRequestDto requestDto = OrderFixture.createOrderRequestWithCoupon();
            User user = UserFixture.createDefaultUser();
            Product product = ProductFixture.productWithStock(100L);
            Coupon coupon = CouponFixture.defaultCoupon();
            CouponPolicy policy = CouponPolicyFixture.defaultPolicy();
            Order order = OrderFixture.defaultOrder();

            when(userReader.getUser(requestDto.userId())).thenReturn(user);
            when(productReader.getById(any())).thenReturn(product);
            when(couponReader.findByIdAndUserId(anyLong(), anyLong())).thenReturn(coupon);
            when(couponPolicyReader.findByIdOrThrow(anyLong())).thenReturn(policy);
            when(policy.calculateDiscount(anyLong())).thenReturn(500L);
            when(orderService.createOrder(any(), any(), anyLong(), any())).thenReturn(order);

            // when
            OrderCreatedResponseDto result = orderFacade.createOrder(requestDto);

            // then
            assertNotNull(result);
            verify(user).usePoint(anyLong());
            verify(externalOrderSender).send(any(ExternalOrderRequestDto.class));
        }

        @Test
        @DisplayName("실패: 포인트 부족")
        void fail_when_insufficient_point() {
            // given
            OrderRequestDto requestDto = OrderFixture.createOrderRequestWithoutCoupon();
            User user = UserFixture.userWithInsufficientPoint();
            Product product = ProductFixture.productWithStock(100L);

            when(userReader.getUser(requestDto.userId())).thenReturn(user);
            when(productReader.getById(any())).thenReturn(product);

            doThrow(new IllegalArgumentException("포인트 부족")).when(user).usePoint(anyLong());

            // when & then
            assertThrows(IllegalArgumentException.class, () -> orderFacade.createOrder(requestDto));
        }

        @Test
        @DisplayName("실패: 쿠폰 사용 불가 (만료 등)")
        void fail_when_coupon_invalid() {
            // given
            OrderRequestDto requestDto = OrderFixture.createOrderRequestWithCoupon();
            User user = UserFixture.createDefaultUser();
            Product product = ProductFixture.productWithStock(100L);
            Coupon coupon = CouponFixture.invalidCoupon(); // 예외 발생하도록 설계됨

            when(userReader.getUser(requestDto.userId())).thenReturn(user);
            when(productReader.getById(any())).thenReturn(product);
            when(couponReader.findByIdAndUserId(anyLong(), anyLong())).thenReturn(coupon);

            doThrow(new IllegalStateException("쿠폰 사용 불가")).when(coupon).markUsed();

            // when & then
            assertThrows(IllegalStateException.class, () -> orderFacade.createOrder(requestDto));
        }
    }

    @Nested
    @DisplayName("인기상품 조회(getPopularProducts) -")
    class GetPopularProducts {

        @Test
        @DisplayName("성공: 판매량 기준 인기 상품 조회")
        void success_popular_products() {
            // given
            ProductSalesDto dto = new ProductSalesDto(1L, 20L);
            when(orderItemRepository.findTopSellingProducts(any(), any()))
                    .thenReturn(List.of(dto));

            Product product = ProductFixture.create(dto.productId(), "테스트상품", 1000L);
            when(productReader.getById(dto.productId()))
                    .thenReturn(product);  // productReader에서 실제 product 객체 반환

            // when
            List<PopularProductResponseDto> result = orderFacade.getPopularProducts(7, 5);

            // then
            assertEquals(1, result.size());
            assertEquals(dto.productId(), result.get(0).productId());
            assertEquals("테스트상품", result.get(0).name());
            assertEquals(20L, result.get(0).totalSold());
        }


    }
}
