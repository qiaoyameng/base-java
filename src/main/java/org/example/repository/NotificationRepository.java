package org.example.repository;

import org.example.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByCustomerId(Long customerId, Pageable pageable);

    List<Notification> findByCustomerIdAndIsRead(Long customerId, Boolean isRead);

    long countByCustomerIdAndIsRead(Long customerId, Boolean isRead);

    boolean existsByOrderIdAndType(Long orderId, Notification.NotificationType type);

    List<Notification> findByOrderId(Long orderId);

    void deleteByCustomerIdAndIsRead(Long customerId, Boolean isRead);
}
