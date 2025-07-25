SET NAMES utf8mb4;

CREATE TABLE users (
    user_id BIGINT PRIMARY KEY COMMENT '사용자 ID',
    point BIGINT NOT NULL COMMENT '보유 포인트',
    created_dt DATETIME NOT NULL COMMENT '가입 일시',
    version BIGINT DEFAULT 0 NOT NULL COMMENT 'Optimistic Lock용'
) COMMENT='회원 정보 및 보유 포인트';

CREATE TABLE coupon_policies (
    coupon_policy_id BIGINT PRIMARY KEY COMMENT '쿠폰 정책 ID',
    name VARCHAR(100) NOT NULL COMMENT '쿠폰명',
    discount_rate BIGINT NOT NULL COMMENT '할인율 (%)',
    issued_total BIGINT NOT NULL COMMENT '총 발급 수량',
    issued_remain BIGINT NOT NULL COMMENT '잔여 수량',
    expired_dt DATETIME NOT NULL COMMENT '만료 일시',
    version BIGINT DEFAULT 0 NOT NULL COMMENT 'Optimistic Lock용'
) COMMENT='쿠폰 정책 정보 (선착순 발급용)';

CREATE TABLE coupons (
    coupon_id BIGINT UNIQUE PRIMARY KEY COMMENT '쿠폰 ID',
    coupon_policy_id BIGINT NOT NULL COMMENT '쿠폰 정책 ID',
    user_id BIGINT NOT NULL COMMENT '사용자 ID',
    name VARCHAR(100) NOT NULL COMMENT '쿠폰명',
    discount_rate BIGINT NOT NULL COMMENT '할인율 (%)',
    issued_dt DATETIME NOT NULL COMMENT '발급 일시',
    used_dt DATETIME COMMENT '사용 일시',
    is_used BOOLEAN NOT NULL COMMENT '사용 여부',
    FOREIGN KEY (coupon_policy_id) REFERENCES coupon_policies(coupon_policy_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    CONSTRAINT uq_user_policy UNIQUE (user_id, coupon_policy_id)
) COMMENT='사용자에게 발급된 쿠폰';

CREATE TABLE point_histories (
    point_history_id BIGINT PRIMARY KEY COMMENT '포인트 이력 ID',
    user_id BIGINT NOT NULL COMMENT '사용자 ID',
    amount BIGINT NOT NULL COMMENT '포인트 금액',
    type VARCHAR(20) NOT NULL COMMENT 'CHARGE(충전)/USE(사용)',
    created_dt DATETIME NOT NULL COMMENT '이력 생성 일시',
    balance_after BIGINT NOT NULL   COMMENT '거래 후 잔액',
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) COMMENT='포인트 충전 및 사용 이력';

CREATE TABLE products (
    product_id BIGINT PRIMARY KEY COMMENT '상품 ID',
    name VARCHAR(100) NOT NULL COMMENT '상품명',
    price BIGINT NOT NULL COMMENT '가격',
    stock BIGINT NOT NULL COMMENT '재고 수량'
) COMMENT='상품 기본 정보';

CREATE TABLE orders (
    order_id BIGINT PRIMARY KEY COMMENT '주문 ID',
    user_id BIGINT NOT NULL COMMENT '사용자 ID',
    total_price BIGINT NOT NULL COMMENT '총 결제 금액',
    status VARCHAR(20) NOT NULL COMMENT '주문 상태',
    ordered_dt DATETIME NOT NULL COMMENT '주문 일시',
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) COMMENT='주문 정보 (마스터)';

CREATE TABLE order_items (
    order_item_id BIGINT PRIMARY KEY COMMENT '주문 상품 항목 ID',
    order_id BIGINT NOT NULL COMMENT '주문 ID',
    product_id BIGINT NOT NULL COMMENT '상품 ID',
    product_name VARCHAR(100) NOT NULL COMMENT '상품명',
    quantity BIGINT NOT NULL COMMENT '수량',
    unit_price BIGINT NOT NULL COMMENT '상품 개별금액',
    total_price BIGINT NOT NULL COMMENT '총 금액',
    status VARCHAR(20) NOT NULL COMMENT '주문 상태',
    ordered_dt DATETIME NOT NULL COMMENT '주문 일시',
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
) COMMENT='주문 내 개별 상품 항목 정보';

CREATE TABLE order_payments (
    order_payment_id BIGINT PRIMARY KEY COMMENT '결제 정보 ID',
    order_id BIGINT NOT NULL UNIQUE COMMENT '주문 ID',
    coupon_id BIGINT COMMENT '사용된 쿠폰 ID',
    original_price BIGINT NOT NULL COMMENT '총 원가 금액',
    discount_amount BIGINT NOT NULL COMMENT '할인 금액',
    final_price BIGINT NOT NULL COMMENT '최종 결제 금액',
    paid_dt DATETIME NOT NULL COMMENT '결제 일시',
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id)
) COMMENT='결제 금액 및 사용 쿠폰 정보';