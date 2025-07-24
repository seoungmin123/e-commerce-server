package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.domain.UserRepository;
import kr.hhplus.be.server.user.dto.PointBalanceDto;
import kr.hhplus.be.server.user.dto.UserResponseDto;
import kr.hhplus.be.server.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserResponseDto> getUser() {
        // 전체 사용자 조회
        List<UserResponseDto> user = userRepository.findAll()
                .stream()
                .map(UserResponseDto::from).toList();
        return user;
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(Long userId) {
        // userId로 사용자 조회
        UserResponseDto userInfo = userRepository.findById(userId)
                .map(UserResponseDto::from)
                .orElseThrow(()->new UserNotFoundException(userId));

        return userInfo;
    }

    //포인트 잔액 조회
    @Transactional(readOnly = true)
    public PointBalanceDto getPointBalance(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        return PointBalanceDto.from(user);
    }


}