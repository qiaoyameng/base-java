package org.example.repository;

import org.example.entity.Order;
import org.example.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Optional<Order> findByOrderNo(String orderNo);

    Page<Order> findByCustomerIdAndDeletedFalse(Long customerId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.deleted = false " +
            "AND (:orderNo IS NULL OR o.orderNo LIKE %:orderNo%) " +
            "AND (:customerId IS NULL OR o.customerId = :customerId) " +
            "AND (:customerName IS NULL OR o.customerName LIKE %:customerName%) " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:paymentMethod IS NULL OR o.paymentMethod = :paymentMethod) " +
            "AND (:startTime IS NULL OR o.createTime >= :startTime) " +
            "AND (:endTime IS NULL OR o.createTime <= :endTime)")
    Page<Order> findByConditions(@Param("orderNo") String orderNo,
                                  @Param("customerId") Long customerId,
                                  @Param("customerName") String customerName,
                                  @Param("status") OrderStatus status,
                                  @Param("paymentMethod") String paymentMethod,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime,
                                  Pageable pageable);

    @Modifying
    @Query("UPDATE Order o SET o.status = :newStatus WHERE o.id = :orderId AND o.status = :currentStatus")
    int updateStatus(@Param("orderId") Long orderId,
                     @Param("currentStatus") OrderStatus currentStatus,
                     @Param("newStatus") OrderStatus newStatus);

    List<Order> findByStatusAndNotificationSentFalseAndPreparedTimeBefore(OrderStatus status, LocalDateTime time);

    List<Order> findByStatusAndPaidTimeBefore(OrderStatus status, LocalDateTime time);
}
