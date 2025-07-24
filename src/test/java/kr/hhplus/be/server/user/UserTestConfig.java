package kr.hhplus.be.server.user;

import kr.hhplus.be.server.user.facade.UserPointFacade;
import kr.hhplus.be.server.user.reader.UserReader;
import kr.hhplus.be.server.pointHistory.appender.PointHistoryAppender;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserTestConfig {

    @Bean
    public UserReader userReader() {
        return Mockito.mock(UserReader.class);
    }

    @Bean
    public PointHistoryAppender pointHistoryAppender() {
        return Mockito.mock(PointHistoryAppender.class);
    }

    @Bean
    public UserPointFacade userPointFacade(UserReader userReader, PointHistoryAppender pointHistoryAppender) {
        return new UserPointFacade(userReader, pointHistoryAppender);
    }
}
