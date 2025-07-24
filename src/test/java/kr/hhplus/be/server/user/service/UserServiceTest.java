package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.UserRepository;
import kr.hhplus.be.server.user.dto.UserResponseDto;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void 유저_전체조회() {
        given(userRepository.findAll()).willReturn(List.of(new User(1L,1000L)));

        List<UserResponseDto> users = userService.getUser();

        assertThat(users).hasSize(1);
    }

    @Test
    void 유저_ID로_조회_정상() {
        User user = new User(1L,1000L);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        UserResponseDto dto = userService.getUserInfo(1L);

        assertThat(dto.userId()).isEqualTo(user.getUserId());
    }

    @Test
    void 유저_ID로_조회시_없으면_예외() {
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserInfo(1L))
                .isInstanceOf(UserNotFoundException.class);
    }
}