package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.coupon.exception.CouponAlreadyUsedException;
import kr.hhplus.be.server.coupon.exception.CouponExpiredException;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons" ,
        uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "coupon_policy_id"}) })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(nullable = false)
    private Long couponPolicyId; // 어떤 정책으로 발급된 쿠폰인지 (단순 FK)

    @Column(nullable = false)
    private Long userId; // 소유자 ID (연관관계 아님)

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int discountRate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime issuedDt;

    @Column(nullable = false)
    private LocalDateTime expiredDt;

    @Column(nullable = false)
    private Boolean isUsed;

    private LocalDateTime usedDt;

    public Coupon(Long couponPolicyId, Long userId, String name, int discountRate, LocalDateTime expiredDt) {
        this.couponPolicyId = couponPolicyId;
        this.userId = userId;
        this.name = name;
        this.discountRate = discountRate;
        this.issuedDt = LocalDateTime.now();
        this.expiredDt = expiredDt;
        this.isUsed = false;
    }

    public static Coupon issue(CouponPolicy policy, Long userId) {
        return new Coupon(
                policy.getCouponPolicyId(),
                userId,
                policy.getName(),
                policy.getDiscountRate(),
                policy.getExpiredDt()
        );
    }

    // 쿠폰 사용 처리 메서드
    public void markUsed() {
        if (isExpired()) {
            throw new CouponExpiredException(this.couponId);
        }
        if (Boolean.TRUE.equals(this.isUsed)) {
            throw new CouponAlreadyUsedException(this.couponId);
        }
        this.isUsed = true;
        this.usedDt = LocalDateTime.now();
    }

    // 사용 가능 여부 체크
    public boolean isUsable() {
        return !isUsed && !isExpired();
    }

    // 만료 여부
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredDt);
    }

    /** 주의!
     * 테스트용 쿠폰 생성 메서드 (직접 필드 세팅)
     * 비즈니스 제약을 무시하고 특정 상태의 쿠폰을 구성할 수 있음.
     */
    @org.jetbrains.annotations.TestOnly
    public static Coupon ofTestValues(
            Long couponId,
            Long couponPolicyId,
            Long userId,
            String name,
            int discountRate,
            boolean isUsed,
            LocalDateTime issuedDt,
            LocalDateTime expiredDt
    ) {
        Coupon coupon = new Coupon();
        coupon.couponId = couponId;
        coupon.couponPolicyId = couponPolicyId;
        coupon.userId = userId;
        coupon.name = name;
        coupon.discountRate = discountRate;
        coupon.issuedDt = issuedDt;
        coupon.expiredDt = expiredDt;
        coupon.isUsed = isUsed;
        return coupon;
    }

}