package org.example.repository;

import org.example.entity.StoredCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoredCardRepository extends JpaRepository<StoredCard, Long> {
    Optional<StoredCard> findByCardNo(String cardNo);
    Optional<StoredCard> findByCustomerId(Long customerId);
}
