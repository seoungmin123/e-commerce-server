package kr.hhplus.be.server.mock.controller.dto;

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
