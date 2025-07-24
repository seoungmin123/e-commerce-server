package kr.hhplus.be.server.order.domain;

import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderItem 도메인 단위 테스트")
class OrderItemTest {

    @Nested
    @DisplayName("정적 팩토리 create(productId, quantity, unitPrice)")
    class Create {

        @Test
        @DisplayName("정상적으로 OrderItem이 생성된다")
        void create_정상_생성() {
            Product product = ProductFixture.macbook();
            OrderItem item = OrderItem.create(product, 2);

            assertThat(item.getProduct().getProductId()).isEqualTo(101L);
            assertThat(item.getProductName()).isEqualTo("맥북");
            assertThat(item.getQuantity()).isEqualTo(2);
            assertThat(item.getUnitPrice()).isEqualTo(1500L);
            assertThat(item.getTotalPrice()).isEqualTo(3000L);
            assertThat(item.getStatus()).isEqualTo(OrderStatus.CREATED);
            assertThat(item.getOrderedDt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("상태 변경: updateStatus")
    class UpdateStatus {

        @Test
        @DisplayName("정상적으로 상태가 변경된다")
        void updateStatus_정상_수행() {
            Product product = ProductFixture.monitor();
            OrderItem item = OrderItem.create(product, 1);

            item.updateStatus(OrderStatus.PAID);

            assertThat(item.getStatus()).isEqualTo(OrderStatus.PAID);
        }

        @Test
        @DisplayName("기존 상태와 동일한 상태로 변경 시 예외 발생")
        void updateStatus_불가능한_상태에서는_예외() {
            Product product = ProductFixture.keyboard();
            OrderItem item = OrderItem.create(product, 1);
            item.updateStatus(OrderStatus.COMPLETED); // 상태: COMPLETED

            assertThatThrownBy(() -> item.updateStatus(OrderStatus.COMPLETED))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("상태 변경이 불가능");
        }
    }
}