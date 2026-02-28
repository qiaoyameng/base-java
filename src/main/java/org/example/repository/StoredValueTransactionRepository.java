package org.example.repository;

import org.example.entity.StoredValueTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoredValueTransactionRepository extends JpaRepository<StoredValueTransaction, Long> {

    Page<StoredValueTransaction> findByMemberIdAndDeletedFalse(Long memberId, Pageable pageable);

    Optional<StoredValueTransaction> findByTransactionNo(String transactionNo);
}
