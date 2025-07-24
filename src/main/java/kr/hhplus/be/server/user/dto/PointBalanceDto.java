package kr.hhplus.be.server.user.dto;

import kr.hhplus.be.server.user.domain.User;

public record PointBalanceDto(
        Long userId,
        Long point
) {
    public static PointBalanceDto from(User user) {
        return new PointBalanceDto(user.getUserId(), user.getPoint());
    }
}