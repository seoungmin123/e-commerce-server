package kr.hhplus.be.server.product.dto;

import kr.hhplus.be.server.order.dto.ProductSalesDto;
import kr.hhplus.be.server.product.reader.ProductReader;

public record PopularProductResponseDto(
        Long productId,
        String name,
        Long totalSold
) {
    public static PopularProductResponseDto from(ProductSalesDto productSalesDto, ProductReader reader) {
        Long productId = productSalesDto.productId();
        Long totalSold = productSalesDto.totalSold();
        String name = reader.getNameById(productId);
        return new PopularProductResponseDto(productId, name, totalSold);
    }
}