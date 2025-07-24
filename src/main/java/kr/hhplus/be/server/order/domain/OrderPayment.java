package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.couponPolicy.domain.CouponPolicy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderPaymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id" , unique = true)
    private Order order;

    private Long couponId;

    @Column(nullable = false)
    private Long originalPrice;

    private Long discountAmount;

    @Column(nullable = false)
    private Long finalPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime paidDt;

    public void setOrder(Order order) {
        this.order = order;
    }

    // 정적 팩토리 메서드
    public static OrderPayment create(Long originalPrice, CouponPolicy policy) {
        OrderPayment payment = new OrderPayment();
        payment.originalPrice = originalPrice;

        if (policy != null) {
            payment.couponId = policy.getCouponPolicyId();
            payment.discountAmount = policy.calculateDiscount(originalPrice);
        } else {
            payment.discountAmount = 0L;
        }

        payment.finalPrice = originalPrice - payment.discountAmount;
        payment.paidDt = LocalDateTime.now();

        return payment;
    }

}