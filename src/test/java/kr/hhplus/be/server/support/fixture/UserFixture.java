package kr.hhplus.be.server.support.fixture;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.dto.PointBalanceDto;
import kr.hhplus.be.server.user.dto.PointChargeRequestDto;
import kr.hhplus.be.server.user.dto.PointUseRequestDto;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserFixture {

    // 가장 기본적인 유저
    public static User createDefaultUser() {
        return new User(1L, 1000L);
    }

    // 유저 ID와 포인트 지정
    public static User createUser(Long userId, Long point) {
        return new User(userId, point);
    }

    public static PointChargeRequestDto charge(Long amount) {
        return new PointChargeRequestDto(amount);
    }

    public static PointUseRequestDto use(Long amount) {
        return new PointUseRequestDto(amount);
    }

    public static PointBalanceDto balanceOf(Long userId, long amount) {
        return new PointBalanceDto(userId, amount);
    }

    // 포인트 부족한 유저
    public static User userWithInsufficientPoint() {
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(1L);
        doThrow(new IllegalArgumentException("포인트 부족"))
                .when(user).usePoint(anyLong());
        return user;
    }

}
