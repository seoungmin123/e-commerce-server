package kr.hhplus.be.server.user.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.order.domain.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-increment
    private Long userId;

    @Column(nullable = false)
    private Long point; // 보유 포인트

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDt; // 가입 시각

    @Version // Optimistic Lock 처리
    private Long version;

    // 생성자 : 포인트 초기화 및 가입 시간 설정
    public User(Long userId, Long point) {
        this.userId = userId;
        this.point = point;
        this.createdDt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user")  // cascade 제거!
    private List<Order> orders = new ArrayList<>();

    /**
     * TODO : 질문 합치라고하셨는데 user 안에 히스토리 도메인이 함께있어도 되는지?
     * User는 현재를 담당하고, PointHistory는 과거를 추적함 -> 다른 책임 (패키지나눔)
     * User는 유저가 가입하면 생기고, 탈퇴하면 사라져
     *
     * PointHistory는 포인트를 충전하거나 사용할 때만 생겨
     * 심지어 유저가 탈퇴해도, PointHistory는 기록용으로 계속 남길 수도 있음
     * -> 생명주기 다름
     * */
    // 포인트 충전: 포인트 증가 및 결과 객체 반환
    public void chargePoint(Long amount) {
        validateCharge(amount);
        this.point += amount;
    }

    // 포인트 사용: 잔액 확인 후 차감 및 결과 객체 반환
    public void usePoint(Long amount) {
        validateUse(amount);
        this.point -= amount;
    }

    // 충전 유효성 검증: null 또는 0 이하 금액 금지
    private void validateCharge(Long amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
    }

    // 사용 유효성 검증: 금액 유효성 + 잔액 부족 여부 확인
    private void validateUse(Long amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("사용 금액은 0보다 커야 합니다.");
        }
        if (this.point < amount) {
            throw new IllegalArgumentException("잔액 부족: 현재 포인트 " + point);
        }
    }

}