package kr.hhplus.be.server.pointHistory.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "point_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory {

    @Id // 기본 키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long amount; // 충전 or 사용된 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType type; // CHARGE / USE

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDt; // 기록 시각

    @Column(nullable = false)
    private Long balanceAfter; // 이 변경 이후의 잔액


    // User.chargePoint : 히스토리 생성 - 충전 기록용
    public static PointHistory charge(User user, Long amount, Long balanceAfter) {
        return new PointHistory(user, amount, PointType.CHARGE, balanceAfter);
    }

    // User.usePoint : 히스토리 생성 - 사용 기록용
    public static PointHistory use(User user, Long amount, Long balanceAfter) {
        return new PointHistory(user, amount, PointType.USE, balanceAfter);
    }

    // 내부 생성자: 공통 초기화 로직
    private PointHistory(User user, Long amount, PointType type, Long balanceAfter) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.createdDt = LocalDateTime.now();
        this.balanceAfter = balanceAfter;
    }
}