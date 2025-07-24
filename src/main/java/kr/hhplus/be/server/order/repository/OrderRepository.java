package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{
    List<Order> findAllByUserUserId(Long userId);

    @Query("""
        SELECT o FROM Order o
        LEFT JOIN FETCH o.orderItems
        WHERE o.orderId = :orderId
    """)
    Optional<Order> findByIdWithOrderItems(@Param("orderId") Long orderId);
}