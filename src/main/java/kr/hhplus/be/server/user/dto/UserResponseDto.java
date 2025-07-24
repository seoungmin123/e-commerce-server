package kr.hhplus.be.server.user.dto;

import kr.hhplus.be.server.user.domain.User;

import java.time.LocalDateTime;

/**
 * 추후 배송지 등 확장 가능성 고려 DTO 분리
 * */
public record UserResponseDto(
        Long userId,
        Long point,
        LocalDateTime createdDt
) {

    // User -> DTO 변환 생성자
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getPoint(),
                user.getCreatedDt()
        );
    }
}