package kr.hhplus.be.server.product.domain;

import kr.hhplus.be.server.order.dto.ProductSalesDto;
import kr.hhplus.be.server.product.dto.PopularProductResponseDto;
import kr.hhplus.be.server.product.reader.ProductReader;
import org.springframework.test.util.ReflectionTestUtils;

public class ProductFixture {

    public static Product create(Long id, String name, Long price) {
        Product product = new Product();
        ReflectionTestUtils.setField(product, "productId", id);
        ReflectionTestUtils.setField(product, "name", name);
        ReflectionTestUtils.setField(product, "price", price);
        return product;
    }

    public static Product macbook() {
        return create(1L, "맥북", 2000000L);
    }

    public static Product monitor() {
        return create(2L, "모니터", 500000L);
    }

    public static Product keyboard() {
        return create(3L, "키보드", 100000L);
    }

    public static Product cherry() {
        return create(4L, "체리", 1000L);
    }

    public static ProductSalesDto 판매정보(Long productId, Long totalSold) {
        return new ProductSalesDto(productId, totalSold);
    }

    public static PopularProductResponseDto 인기상품응답(Long productId, Long totalSold, String name) {
        return new PopularProductResponseDto(productId, name, totalSold);
    }

    public static PopularProductResponseDto 인기상품응답(ProductSalesDto dto, ProductReader reader) {
        return PopularProductResponseDto.from(dto, reader);
    }
}