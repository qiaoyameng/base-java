package org.example.repository;

import org.example.entity.ExperienceShare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceShareRepository extends JpaRepository<ExperienceShare, Long> {
    Page<ExperienceShare> findByDeletedFalseOrderByCreateTimeDesc(Pageable pageable);

    Page<ExperienceShare> findByMemberIdAndDeletedFalse(Long memberId, Pageable pageable);

    Page<ExperienceShare> findByProductIdAndDeletedFalse(Long productId, Pageable pageable);

    @Query("SELECT e FROM ExperienceShare e WHERE e.deleted = false ORDER BY e.likes DESC")
    Page<ExperienceShare> findPopular(Pageable pageable);

    @Query("SELECT e FROM ExperienceShare e WHERE e.deleted = false AND e.title LIKE %:keyword% OR e.content LIKE %:keyword%")
    Page<ExperienceShare> search(@Param("keyword") String keyword, Pageable pageable);
}
