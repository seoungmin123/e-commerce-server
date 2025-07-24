package kr.hhplus.be.server.support.fixture;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.dto.PointBalanceDto;
import kr.hhplus.be.server.user.dto.PointChargeRequestDto;
import kr.hhplus.be.server.user.dto.PointUseRequestDto;

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

}
