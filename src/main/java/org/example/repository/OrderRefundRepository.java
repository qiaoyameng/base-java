package org.example.repository;

import org.example.entity.OrderRefund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRefundRepository extends JpaRepository<OrderRefund, Long> {
    Optional<OrderRefund> findByOrderId(Long orderId);

    Page<OrderRefund> findByStatus(String status, Pageable pageable);

    Page<OrderRefund> findByMemberIdOrderByCreateTimeDesc(Long memberId, Pageable pageable);

    List<OrderRefund> findByOrderIdIn(List<Long> orderIds);
}
