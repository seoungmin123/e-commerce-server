package kr.hhplus.be.server.order.domain;

import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import kr.hhplus.be.server.support.fixture.UserFixture;
import kr.hhplus.be.server.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("OrderPayment 도메인 단위 테스트")
class OrderPaymentTest {

    @Nested
    @DisplayName("정적 팩토리 create(couponId, originalPrice, discountAmount)")
    class Create {

        @Test
        @DisplayName("정상적으로 생성된다 (할인 없음)")
        void create_정상_생성_할인없음() {
            OrderPayment payment = OrderPayment.create(10000L, null);

            assertThat(payment.getCouponId()).isNull();
            assertThat(payment.getOriginalPrice()).isEqualTo(10000L);
            assertThat(payment.getDiscountAmount()).isEqualTo(0L);
            assertThat(payment.getFinalPrice()).isEqualTo(10000L);
        }

        @Test
        @DisplayName("정상적으로 생성된다 (할인 있음)")
        void create_정상_생성_할인있음() {
            // given
            CouponPolicy policy = mock(CouponPolicy.class);
            when(policy.getCouponPolicyId()).thenReturn(100L);
            when(policy.calculateDiscount(10000L)).thenReturn(3000L);

            // when
            OrderPayment payment = OrderPayment.create(10000L, policy);

            // then
            assertThat(payment.getCouponId()).isEqualTo(100L);
            assertThat(payment.getOriginalPrice()).isEqualTo(10000L);
            assertThat(payment.getDiscountAmount()).isEqualTo(3000L);
            assertThat(payment.getFinalPrice()).isEqualTo(7000L);
        }
    }

    @Nested
    @DisplayName("연관관계 설정: setOrder")
    class SetOrder {

        @Test
        @DisplayName("Order 객체를 연관관계로 설정할 수 있다")
        void setOrder_연관관계_설정() {
            User user = UserFixture.createDefaultUser();
            Order order = new Order(user, 3000L);
            OrderPayment payment = OrderPayment.create(3000L, null);

            payment.setOrder(order);

            assertThat(payment.getOrder()).isEqualTo(order);
        }
    }
}