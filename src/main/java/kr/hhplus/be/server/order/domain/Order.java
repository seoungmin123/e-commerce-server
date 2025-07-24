package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.order.exception.InvalidOrderStatusTransitionException;
import kr.hhplus.be.server.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedDt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderPayment orderPayment;

    public Order(User user, Long totalPrice) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.CREATED;
        this.orderedDt = LocalDateTime.now();
    }


    // 주문 : 정적 팩토리 메서드
    public static Order create(User user, List<OrderItem> items, OrderPayment payment) {
        Long totalPrice = calculateTotalPrice(items);

        Order order = new Order(user, totalPrice);

        for (OrderItem item : items) {
            order.addOrderItem(item);
        }

        order.setOrderPayment(payment);

        return order;
    }

    // 총합 계산
    private static Long calculateTotalPrice(List<OrderItem> items) {
        return items.stream()
                .mapToLong(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }

    // 양방향 연관관계 편의 메서드
    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
        item.setOrder(this);
    }

    public void setOrderPayment(OrderPayment payment) {
        this.orderPayment = payment;
        payment.setOrder(this);
    }

    // 상태값 변경 : 결제 성공 처리 -> 지금 안씀
    public void markAsPaid() {
        if (this.status != OrderStatus.CREATED) {
            throw new InvalidOrderStatusTransitionException(this.status, "결제 완료 처리");
        }
        this.status = OrderStatus.PAID;
        // OrderItem 들도 상태 변경
        this.orderItems.forEach(item -> item.updateStatus(OrderStatus.PAID));
    }

    //상태값 변경 : 주문 완료 처리
    public void markAsCompleted() {
       /* TODO 현재 상태 단순 변경으로 변경함 추후 구현
       if (this.status != OrderStatus.PAID) {
            throw new InvalidOrderStatusTransitionException(this.status, "주문 완료 처리");
        }*/
        this.status = OrderStatus.COMPLETED;

        // OrderItem 들도 상태 변경
        this.orderItems.forEach(item -> item.updateStatus(OrderStatus.COMPLETED));
    }

    //상태값 변경 : 결제 실패
    public void markAsPaymentFailed() {
        this.status = OrderStatus.PAYMENT_FAILED;
        // OrderItem 들도 상태 변경
        this.orderItems.forEach(item -> item.updateStatus(OrderStatus.PAYMENT_FAILED));
    }
}