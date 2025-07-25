package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.order.dto.ProductListResponseDto;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductRepository;
import kr.hhplus.be.server.product.dto.ProductDetailResponseDto;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("전체 상품 목록을 조회한다")
    void getAllProducts_success() {
        // given
        Product p1 = new Product("상품1", 1000L, 10);
        Product p2 = new Product("상품2", 2000L, 20);
        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // when
        List<ProductListResponseDto> result = productService.getAllProducts();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("상품1");
        assertThat(result.get(1).price()).isEqualTo(2000L);
        verify(productRepository).findAll(); // 호출 여부 확인
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void getProductDetail_success() {
        // given
        Product product = new Product("테스트 상품", 1500L, 5);
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        ProductDetailResponseDto result = productService.getProductDetail(productId);

        // then
        assertThat(result.name()).isEqualTo("테스트 상품");
        assertThat(result.stock()).isEqualTo(5);
        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("상품이 존재하지 않으면 예외 발생")
    void getProductDetail_fail_notFound() {
        // given
        Long productId = 99L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.getProductDetail(productId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(productId.toString());

        verify(productRepository).findById(productId);
    }
}