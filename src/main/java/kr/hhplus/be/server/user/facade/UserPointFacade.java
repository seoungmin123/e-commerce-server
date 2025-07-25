package kr.hhplus.be.server.user.facade;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.pointHistory.appender.PointHistoryAppender;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.dto.PointBalanceDto;
import kr.hhplus.be.server.user.dto.PointChargeRequestDto;
import kr.hhplus.be.server.user.dto.PointUseRequestDto;
import kr.hhplus.be.server.user.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserPointFacade {

    private final UserReader userReader;
    private final PointHistoryAppender pointHistoryAppender;

    //포인트 충전
    @Transactional
    public PointBalanceDto chargePoint(Long userId, PointChargeRequestDto request) {
        User userInfo = userReader.getUser(userId);
        userInfo.chargePoint(request.amount());

        pointHistoryAppender.appendCharge(userInfo, request.amount(), userInfo.getPoint());

        return PointBalanceDto.from(userInfo);
    }

    //포인트 사용
    @Transactional
    public PointBalanceDto usePoint(Long userId, PointUseRequestDto request) {
        User userInfo = userReader.getUser(userId);
        userInfo.usePoint(request.amount());

        pointHistoryAppender.appendUse(userInfo, request.amount(), userInfo.getPoint());

        return PointBalanceDto.from(userInfo);
    }
}