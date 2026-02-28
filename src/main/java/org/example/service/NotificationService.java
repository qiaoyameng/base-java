package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Notification;
import org.example.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Page<Notification> findByCustomerId(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return notificationRepository.findByCustomerId(customerId, pageable);
    }

    public List<Notification> findUnread(Long customerId) {
        return notificationRepository.findByCustomerIdAndIsRead(customerId, false);
    }

    public long countUnread(Long customerId) {
        return notificationRepository.countByCustomerIdAndIsRead(customerId, false);
    }

    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Transactional
    public Notification read(Long id) {
        Notification notification = findById(id);
        if (notification != null) {
            notification.setIsRead(true);
            return notificationRepository.save(notification);
        }
        return null;
    }

    @Transactional
    public void readAll(Long customerId) {
        List<Notification> unread = notificationRepository.findByCustomerIdAndIsRead(customerId, false);
        for (Notification n : unread) {
            n.setIsRead(true);
        }
        notificationRepository.saveAll(unread);
    }

    @Transactional
    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void deleteRead(Long customerId) {
        notificationRepository.deleteByCustomerIdAndIsRead(customerId, true);
    }
}
