package kr.hhplus.be.server.couponPolicy.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.couponPolicy.exception.InsufficientCouponQuantityException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_policies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponPolicyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int discountRate;

    @Column(nullable = false)
    private Long issuedTotal; // 총 발급 수량

    @Column(nullable = false)
    private Long issuedRemain; // 남은 수량

    @Column(nullable = false)
    private LocalDateTime expiredDt;

    @Version
    private Long version; // 낙관적 락 (중복 발급, 수량 중복 대비)

    //만료 여부
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredDt);
    }

    // 수량 차감
    public void decreaseIssuedRemain() {
        if (issuedRemain <= 0) {
            throw new IllegalStateException("쿠폰 수량이 모두 소진되었습니다.");
        }
        this.issuedRemain -= 1;
    }

    // 쿠폰 발급 가능 수량 검증
    public void validateIssueAvailability() {
        if (this.issuedRemain <= 0) {
            throw new InsufficientCouponQuantityException();
        }
    }

    // 쿠폰 잔여수량
    public long getRemainingQuantity() {
        return this.issuedRemain;
    }

    // 사용자 쿠폰 발급시 동일정책의 쿠폰이 있는지 확인
    public boolean isQuantityAvailable() {
        return this.issuedRemain  > 0;
    }

    // 할인 금액 계산
    public Long calculateDiscount(Long originalPrice) {
        if (originalPrice == null || originalPrice <= 0) {
            return 0L;
        }
        if (this.discountRate <= 0) {
            return 0L;
        }
        return originalPrice * this.discountRate / 100;
    }

    /** 주의!
     * 테스트용 쿠폰 정책 생성 메서드 (직접 필드 세팅)
     * 비즈니스 제약을 무시하고 특정 상태의 쿠폰 정책을 구성할 수 있음.
     */
    @org.jetbrains.annotations.TestOnly
    public static CouponPolicy ofTestValues(
            Long couponPolicyId,
            String name,
            int discountRate,
            Long issuedTotal,
            Long issuedRemain,
            LocalDateTime expiredDt
    ) {
        CouponPolicy policy = new CouponPolicy();
        policy.couponPolicyId = couponPolicyId;
        policy.name = name;
        policy.discountRate = discountRate;
        policy.issuedTotal = issuedTotal;
        policy.issuedRemain = issuedRemain;
        policy.expiredDt = expiredDt;
        return policy;
    }

}