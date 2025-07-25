package kr.hhplus.be.server.order.dto;

public record OrderItemRequestDto(
        Long productId,
        int quantity,
        Long price
) {}