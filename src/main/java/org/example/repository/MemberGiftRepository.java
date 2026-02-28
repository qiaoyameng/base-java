package org.example.repository;

import org.example.entity.MemberGift;
import org.example.enums.MemberLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberGiftRepository extends JpaRepository<MemberGift, Long> {

    List<MemberGift> findByActiveTrueAndDeletedFalseOrderBySortOrderAsc();

    List<MemberGift> findByRequiredLevelAndActiveTrueAndDeletedFalseOrderBySortOrderAsc(MemberLevel level);

    Page<MemberGift> findByActiveTrueAndDeletedFalse(Pageable pageable);
}
