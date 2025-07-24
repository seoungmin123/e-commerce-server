package kr.hhplus.be.server.pointHistory.service;

import kr.hhplus.be.server.pointHistory.domain.PointHistory;
import kr.hhplus.be.server.pointHistory.domain.PointHistoryRepository;
import kr.hhplus.be.server.pointHistory.dto.PointHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointHistoryRepository pointHistoryRepository;

    //포인트 이력조회
    @Transactional(readOnly = true)
    public List<PointHistoryResponseDto> getPointHistory(Long userId) {
        List<PointHistory> pointHistories = pointHistoryRepository.findByUserUserIdOrderByCreatedDtDesc(userId);

        return pointHistories.stream()
                .map(PointHistoryResponseDto::from)
                .toList();
    }
}