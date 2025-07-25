package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.order.exception.InvalidOrderStatusTransitionException;
import kr.hhplus.be.server.product.domain.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Long unitPrice;

    @Column(nullable = false)
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedDt;

    // TODO 생성 테스트 불가 static 없애도되나..
    public static OrderItem create(Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.product = product;
        item.productName = product.getName(); // 스냅샷
        item.unitPrice = product.getPrice();  // 스냅샷
        item.quantity = quantity;
        item.totalPrice = item.unitPrice * quantity;
        item.status = OrderStatus.CREATED;
        item.orderedDt = LocalDateTime.now();
        return item;
    }

    // 연관관계 설정 (Order에서 호출)
    public void setOrder(Order order) {
        this.order = order;
    }

    // 상태값 변경
    public void updateStatus(OrderStatus status) {
        if (this.status == OrderStatus.COMPLETED || this.status == OrderStatus.PAYMENT_FAILED) {
            throw new InvalidOrderStatusTransitionException(this.status, "상태 변경");
        }
        this.status = status;
    }
}