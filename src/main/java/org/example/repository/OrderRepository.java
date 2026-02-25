package org.example.repository;

import org.example.entity.WashOrder;
import org.example.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<WashOrder, Long> {
    Optional<WashOrder> findByOrderNo(String orderNo);
    List<WashOrder> findByCustomerId(Long customerId);
    List<WashOrder> findByCustomerIdAndStatus(Long customerId, OrderStatus status);
    List<WashOrder> findByStoreId(Long storeId);
    List<WashOrder> findByStatus(OrderStatus status);
    List<WashOrder> findByStatusIn(List<OrderStatus> statuses);

    @Query("SELECT o FROM WashOrder o WHERE o.status = ?1 AND o.estimatedCompleteTime BETWEEN ?2 AND ?3")
    List<WashOrder> findByStatusAndEstimatedCompleteTimeBetween(OrderStatus status, LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT COUNT(o) FROM WashOrder o WHERE o.customer.id = ?1 AND o.status != 'CANCELLED'")
    Long countByCustomerId(Long customerId);

    @Query("SELECT SUM(o.actualAmount) FROM WashOrder o WHERE o.customer.id = ?1 AND o.status = 'COMPLETED'")
    java.math.BigDecimal sumActualAmountByCustomerId(Long customerId);
}
