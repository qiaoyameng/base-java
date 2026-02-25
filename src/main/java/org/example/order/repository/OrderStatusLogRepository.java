package org.example.order.repository;

import org.example.order.entity.OrderStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusLogRepository extends JpaRepository<OrderStatusLog, Long> {
    
    List<OrderStatusLog> findByOrderIdOrderByOperateTimeDesc(Long orderId);
}
