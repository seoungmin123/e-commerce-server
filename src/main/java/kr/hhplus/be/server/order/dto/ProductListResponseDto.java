package kr.hhplus.be.server.order.dto;

import kr.hhplus.be.server.product.domain.Product;

public record ProductListResponseDto(
        Long productId,
        String name,
        Long price,
        int stock
) {
    public static ProductListResponseDto from(Product product) {
        return new ProductListResponseDto(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getStock());
    }
}