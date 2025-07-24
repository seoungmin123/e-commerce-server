package kr.hhplus.be.server.product.dto;

import kr.hhplus.be.server.product.domain.Product;

public record ProductDetailResponseDto(
        Long productId,
        String name,
        Long price,
        int stock
) {
    public static ProductDetailResponseDto from(Product product) {
        return new ProductDetailResponseDto(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getStock());
    }
}