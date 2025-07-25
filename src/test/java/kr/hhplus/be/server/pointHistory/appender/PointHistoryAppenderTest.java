package kr.hhplus.be.server.pointHistory.appender;

import kr.hhplus.be.server.pointHistory.domain.PointHistoryRepository;
import kr.hhplus.be.server.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@DisplayName("PointHistoryAppender 테스트")
class PointHistoryAppenderTest {

    private PointHistoryRepository pointHistoryRepository;
    private PointHistoryAppender pointHistoryAppender;

    @BeforeEach
    void setUp() {
        pointHistoryRepository = mock(PointHistoryRepository.class);
        pointHistoryAppender = new PointHistoryAppender(pointHistoryRepository);
    }

    @Nested
    @DisplayName("포인트 충전 이력 기록")
    class AppendCharge {

        @Test
        @DisplayName("정상적으로 포인트 충전 이력을 저장한다")
        void appendCharge() {
            // given
            User user = new User(1L, 0L);
            Long amount = 1000L;
            Long balanceAfter = 1000L;

            // when
            pointHistoryAppender.appendCharge(user, amount, balanceAfter);

            // then
            verify(pointHistoryRepository).save(any());
        }
    }

    @Nested
    @DisplayName("포인트 사용 이력 기록")
    class AppendUse {

        @Test
        @DisplayName("정상적으로 포인트 사용 이력을 저장한다")
        void appendUse() {
            // given
            User user = new User(1L, 1000L);
            Long amount = 500L;
            Long balanceAfter = 500L;

            // when
            pointHistoryAppender.appendUse(user, amount, balanceAfter);

            // then
            verify(pointHistoryRepository).save(any());
        }
    }
}