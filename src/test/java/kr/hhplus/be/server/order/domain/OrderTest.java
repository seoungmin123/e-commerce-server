package kr.hhplus.be.server.order.domain;

import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductFixture;
import kr.hhplus.be.server.support.fixture.UserFixture;
import kr.hhplus.be.server.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@DisplayName("Order 도메인 단위 테스트")
class OrderTest {

    @Nested
    @DisplayName("정적 팩토리 Order.create")
    class Create {

        @Test
        @DisplayName("정상적으로 주문 객체 생성에 성공한다")
        void success_when_valid_arguments() {
            // given
            User user = mock(User.class);
            when(user.getUserId()).thenReturn(1L);

            Product cherry = ProductFixture.cherry();

            // OrderItem.create(productId, name, quantity, unitPrice)
            List<OrderItem> items = List.of(OrderItem.create(cherry,2)); // 총 2000원

            // OrderPayment.create(originalPrice, policy)
            OrderPayment payment = OrderPayment.create(2000L, null);  // 할인 없음

            // when
            Order order = Order.create(user, items, payment);

            // then
            assertThat(order.getUser().getUserId()).isEqualTo(1L);
            assertThat(order.getOrderItems()).hasSize(1);
            assertThat(order.getOrderPayment()).isEqualTo(payment);
            assertThat(order.getTotalPrice()).isEqualTo(2000L);
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
            assertThat(order.getOrderedDt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("상태 변경 - markAsPaid")
    class MarkAsPaid {

        @Test
        @DisplayName("CREATED 상태일 때 결제 처리에 성공한다")
        void success_when_created_status() {
            // given
            User user = UserFixture.createDefaultUser();
            Order order = new Order(user, 1000L);

            // when
            order.markAsPaid();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
        }

        @Test
        @DisplayName("CREATED가 아닌 상태에서 결제 시도 시 예외 발생")
        void fail_when_not_created_status() {
            // given
            User user = UserFixture.createDefaultUser();
            Order order = new Order(user, 1000L);
            order.markAsPaid();

            // then
            assertThatThrownBy(order::markAsPaid)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("CREATED 상태에서만");
        }
    }

    @Nested
    @DisplayName("상태 변경 - markAsCompleted")
    class MarkAsCompleted {

        @Test
        @DisplayName("PAID 상태일 때 완료 처리에 성공한다")
        void success_when_paid_status() {
            // given
            User user = UserFixture.createDefaultUser();
            Order order = new Order(user, 1000L);
            order.markAsPaid();

            // when
            order.markAsCompleted();

            // then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        }

        @Test
        @DisplayName("PAID가 아닌 상태에서 완료 처리 시도 시 예외 발생")
        void fail_when_not_paid_status() {
            // given
            User user = UserFixture.createDefaultUser();
            Order order = new Order(user, 1000L);

            // then
            assertThatThrownBy(order::markAsCompleted)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("PAID 상태에서만");
        }
    }
}