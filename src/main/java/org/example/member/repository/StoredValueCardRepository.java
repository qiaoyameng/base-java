package org.example.member.repository;

import org.example.member.entity.StoredValueCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoredValueCardRepository extends JpaRepository<StoredValueCard, Long>, JpaSpecificationExecutor<StoredValueCard> {
    
    List<StoredValueCard> findByMemberId(Long memberId);
    
    Optional<StoredValueCard> findByCardNo(String cardNo);
    
    Optional<StoredValueCard> findByMemberIdAndStatus(Long memberId, Integer status);
}
