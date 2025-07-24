package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.domain.OrderItem;
import kr.hhplus.be.server.order.dto.ProductSalesDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    @Query("""
    SELECT new kr.hhplus.be.server.order.dto.ProductSalesDto(oi.product.productId, SUM(oi.quantity))
    FROM OrderItem oi
    WHERE oi.orderedDt >= :cutoff
    GROUP BY oi.product.productId
    ORDER BY SUM(oi.quantity) DESC
    """)
    List<ProductSalesDto> findTopSellingProducts(@Param("cutoff") LocalDateTime cutoff, Pageable pageable);

}