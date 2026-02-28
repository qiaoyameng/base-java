package org.example.repository;

import org.example.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Optional<Receipt> findByReceiptNo(String receiptNo);

    Optional<Receipt> findByOrderId(Long orderId);

    Optional<Receipt> findByVerificationCode(String verificationCode);
}
