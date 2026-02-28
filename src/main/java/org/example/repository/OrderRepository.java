package org.example.repository;

import org.example.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByOrderNo(String orderNo);
    Page<Orders> findByCustomerId(Long customerId, Pageable pageable);
    Page<Orders> findByCustomerIdAndStatus(Long customerId, Orders.OrderStatus status, Pageable pageable);
    List<Orders> findByStatus(Orders.OrderStatus status);
    
    @Query("SELECT o FROM Orders o WHERE o.status = 'PENDING_PREPARE' AND o.prepareTime <= :time AND o.notifiedPrepare = false")
    List<Orders> findOrdersToNotify(LocalDateTime time);
    
    List<Orders> findByStatusAndCreateTimeBefore(Orders.OrderStatus status, LocalDateTime beforeTime);

    List<Orders> findByStatusAndUpdateTimeBefore(Orders.OrderStatus status, LocalDateTime beforeTime);
}
