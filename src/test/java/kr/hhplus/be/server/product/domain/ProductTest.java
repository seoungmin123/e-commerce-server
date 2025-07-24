package kr.hhplus.be.server.product.domain;

import kr.hhplus.be.server.product.exception.InsufficientStockException;
import kr.hhplus.be.server.product.exception.InvalidStockQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    @DisplayName("정상적으로 재고가 차감된다")
    void deductStock_success() {
        // given
        Product product = new Product("테스트 상품", 1000L, 10);

        // when
        product.deductStock(3);

        // then
        assertThat(product.getStock()).isEqualTo(7);
    }

    @Test
    @DisplayName("재고 차감 시 수량이 0 이하이면 예외 발생")
    void deductStock_invalidQuantity() {
        // given
        Product product = new Product("테스트 상품", 1000L, 10);

        // when & then
        assertThatThrownBy(() -> product.deductStock(0))
                .isInstanceOf(InvalidStockQuantityException.class);
    }

    @Test
    @DisplayName("재고보다 많은 수량을 차감 시도하면 예외 발생")
    void deductStock_insufficientStock() {
        // given
        Product product = new Product("테스트 상품", 1000L, 5);

        // when & then
        assertThatThrownBy(() -> product.deductStock(10))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    @DisplayName("정상적으로 재고가 추가된다")
    void addStock_success() {
        // given
        Product product = new Product("테스트 상품", 1000L, 5);

        // when
        product.addStock(3);

        // then
        assertThat(product.getStock()).isEqualTo(8);
    }

    @Test
    @DisplayName("재고 추가 시 수량이 0 이하이면 예외 발생")
    void addStock_invalidQuantity() {
        // given
        Product product = new Product("테스트 상품", 1000L, 5);

        // when & then
        assertThatThrownBy(() -> product.addStock(0))
                .isInstanceOf(InvalidStockQuantityException.class);
    }
}