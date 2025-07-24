package kr.hhplus.be.server.product.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.product.exception.InsufficientStockException;
import kr.hhplus.be.server.product.exception.InvalidStockQuantityException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products") // 매핑 테이블명 (복수형 사용)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id // 기본 키
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name; // 상품명

    @Column(nullable = false)
    private Long price; // 상품 가격

    @Column(nullable = false)
    private int stock; // 재고 수량

    // 생성자 : 상품 신규 등록 시 사용
    public Product(String name, Long price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // 재고 차감
    public void deductStock(int quantity) {
        validateStock(quantity);
        this.stock -= quantity;
    }

    // 재고 차감 전 유효성 검증
    private void validateStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidStockQuantityException(quantity);
        }
        if (this.stock < quantity) {
            throw new InsufficientStockException(this.productId, this.stock, quantity);
        }
    }

    // TODO : 재고 추가
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidStockQuantityException(quantity);
        }
        this.stock += quantity;
    }
}