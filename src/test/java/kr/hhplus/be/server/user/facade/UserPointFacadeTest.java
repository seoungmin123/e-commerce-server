package kr.hhplus.be.server.user.facade;

import kr.hhplus.be.server.pointHistory.appender.PointHistoryAppender;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.dto.PointBalanceDto;
import kr.hhplus.be.server.user.dto.PointChargeRequestDto;
import kr.hhplus.be.server.user.dto.PointUseRequestDto;
import kr.hhplus.be.server.user.reader.UserReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserPointFacadeTest {

    @InjectMocks
    private UserPointFacade userPointFacade;

    @Mock
    private UserReader userReader;

    @Mock
    private PointHistoryAppender pointHistoryAppender;

    @Test
    void 포인트_충전_성공() {
        User user = new User(1L,1000L);
        given(userReader.getUser(1L)).willReturn(user);

        PointChargeRequestDto request = new PointChargeRequestDto(500L);
        PointBalanceDto result = userPointFacade.chargePoint(1L, request);

        assertThat(result.point()).isEqualTo(1500L);
        verify(pointHistoryAppender).appendCharge(user, 500L, 1500L);
    }

    @Test
    void 포인트_사용_성공() {
        User user = new User(1L,1000L);
        given(userReader.getUser(1L)).willReturn(user);

        PointUseRequestDto request = new PointUseRequestDto(300L);
        PointBalanceDto result = userPointFacade.usePoint(1L, request);

        assertThat(result.point()).isEqualTo(700L);
        verify(pointHistoryAppender).appendUse(user, 300L, 700L);
    }
}