package kr.hhplus.be.server.pointHistory.service;

import kr.hhplus.be.server.pointHistory.domain.PointHistory;
import kr.hhplus.be.server.pointHistory.domain.PointHistoryRepository;
import kr.hhplus.be.server.pointHistory.domain.PointType;
import kr.hhplus.be.server.pointHistory.dto.PointHistoryResponseDto;
import kr.hhplus.be.server.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;


@DisplayName("PointServiceTest 도메인 테스트")
class PointServiceTest {
    @Nested
    @DisplayName("getPointHistory 호출 시")
    class GetPointHistory {

        private PointHistoryRepository pointHistoryRepository; // Mock 대상
        private PointService pointService; // 테스트 대상

        @BeforeEach
        void setUp() {
            pointHistoryRepository = mock(PointHistoryRepository.class);
            pointService = new PointService(pointHistoryRepository);
        }

        @Test
        @DisplayName("유저의 포인트 이력을 최신순으로 반환한다")
        void 포인트_이력_조회() {
            // given
            User user = new User(1L, 0L);

            PointHistory history1 = PointHistory.charge(user, 1000L, 2000L);
            setField(history1, "pointHistoryId", 1L);
            setField(history1, "createdDt", LocalDateTime.of(2024, 5, 1, 10, 0));

            PointHistory history2 = PointHistory.use(user, 500L, 1500L);
            setField(history2, "pointHistoryId", 2L);
            setField(history2, "createdDt", LocalDateTime.of(2024, 5, 2, 12, 0));

            when(pointHistoryRepository.findByUserUserIdOrderByCreatedDtDesc(user.getUserId()))
                    .thenReturn(List.of(history2, history1)); // 최신순

            // when
            List<PointHistoryResponseDto> result = pointService.getPointHistory(user.getUserId());

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).type()).isEqualTo(PointType.USE);
            assertThat(result.get(1).amount()).isEqualTo(1000L);
            verify(pointHistoryRepository).findByUserUserIdOrderByCreatedDtDesc(user.getUserId());
        }
    }
}