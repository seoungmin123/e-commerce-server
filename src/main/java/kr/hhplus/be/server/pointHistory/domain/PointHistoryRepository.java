package kr.hhplus.be.server.pointHistory.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory,Long> {
    public List<PointHistory> findByUserUserIdOrderByCreatedDtDesc(Long userId);
}