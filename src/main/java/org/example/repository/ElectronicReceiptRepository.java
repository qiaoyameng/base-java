package org.example.repository;

import org.example.entity.ElectronicReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectronicReceiptRepository extends JpaRepository<ElectronicReceipt, Long> {
    Optional<ElectronicReceipt> findByOrderId(Long orderId);

    Optional<ElectronicReceipt> findByReceiptNo(String receiptNo);

    Optional<ElectronicReceipt> findByMemberIdAndOrderId(Long memberId, Long orderId);
}
