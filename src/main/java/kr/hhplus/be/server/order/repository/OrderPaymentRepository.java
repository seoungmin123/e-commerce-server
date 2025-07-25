package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.domain.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment,Long> {
    Optional<OrderPayment> findByOrderOrderId(Long orderId);
}