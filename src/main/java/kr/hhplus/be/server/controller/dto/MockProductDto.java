package kr.hhplus.be.server.controller.dto;

public class MockProductDto {

    public record ProductResponseDto(
            Long productId,
            String name,
            int price,
            int stock
    ) {}

    public record PopularProductDto(
            Long productId,
            int totalSold
    ) {}
}
