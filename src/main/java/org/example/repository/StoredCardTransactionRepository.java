package org.example.repository;

import org.example.entity.StoredCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoredCardTransactionRepository extends JpaRepository<StoredCardTransaction, Long> {
    List<StoredCardTransaction> findTop10ByStoredCardIdOrderByCreateTimeDesc(Long cardId);
    List<StoredCardTransaction> findByStoredCardId(Long cardId);
}
