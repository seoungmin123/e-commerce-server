package kr.hhplus.be.server.order.dto;

import java.util.List;

public record OrderRequestDto(
        Long userId,
        List<OrderItemRequestDto> items,
        Long couponId    // 쿠폰 ID만 넘기고 실제 정책은 Reader 통해 조회
) {}