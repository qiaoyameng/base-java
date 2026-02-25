package org.example.order.repository;

import org.example.common.enums.OrderStatus;
import org.example.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    
    Optional<Order> findByOrderNo(String orderNo);
    
    List<Order> findByMemberId(Long memberId);
    
    List<Order> findByMemberIdOrderByCreatedAtDesc(Long memberId);
    
    List<Order> findByStoreId(Long storeId);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime time);
    
    List<Order> findByEmployeeId(Long employeeId);
}
