package kr.hhplus.be.server.pointHistory.appender;

import kr.hhplus.be.server.pointHistory.domain.PointHistory;
import kr.hhplus.be.server.pointHistory.domain.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointHistoryAppender {

    private final PointHistoryRepository pointHistoryRepository;

    public void appendCharge(User user, Long amount, Long balanceAfter) {
        PointHistory history = PointHistory.charge(user, amount, balanceAfter);
        pointHistoryRepository.save(history);
    }

    public void appendUse(User user, Long amount, Long balanceAfter) {
        PointHistory history = PointHistory.use(user, amount, balanceAfter);
        pointHistoryRepository.save(history);
    }
}