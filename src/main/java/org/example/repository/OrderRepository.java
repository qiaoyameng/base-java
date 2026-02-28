package org.example.repository;

import org.example.entity.Order;
import org.example.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndDeletedFalse(Long id);

    Optional<Order> findByOrderNoAndDeletedFalse(String orderNo);

    Page<Order> findByMemberIdAndDeletedFalse(Long memberId, Pageable pageable);

    Page<Order> findByDeletedFalse(Pageable pageable);

    Page<Order> findByStatusAndDeletedFalse(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.deleted = false AND o.memberId = :memberId AND o.status = :status")
    Page<Order> findByMemberIdAndStatusAndDeletedFalse(@Param("memberId") Long memberId, @Param("status") OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.deleted = false AND o.status IN :statuses AND o.preparedTime IS NOT NULL AND o.reminderTime IS NULL")
    List<Order> findOrdersNeedReminder(@Param("statuses") List<OrderStatus> statuses);

    @Query("SELECT o FROM Order o WHERE o.deleted = false AND o.status = :status AND o.createTime < :timeout")
    List<Order> findTimeoutOrders(@Param("status") OrderStatus status, @Param("timeout") LocalDateTime timeout);

    boolean existsByOrderNo(String orderNo);
}
