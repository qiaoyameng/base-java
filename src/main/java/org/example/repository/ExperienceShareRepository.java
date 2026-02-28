package org.example.repository;

import org.example.entity.ExperienceShare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceShareRepository extends JpaRepository<ExperienceShare, Long> {

    Page<ExperienceShare> findByDeletedFalseOrderByCreateTimeDesc(Pageable pageable);

    List<ExperienceShare> findByFeaturedTrueAndDeletedFalseOrderBySortOrderAsc();

    Page<ExperienceShare> findByCustomerIdAndDeletedFalse(Long customerId, Pageable pageable);

    @Modifying
    @Query("UPDATE ExperienceShare e SET e.likeCount = e.likeCount + 1 WHERE e.id = :id")
    int incrementLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ExperienceShare e SET e.commentCount = e.commentCount + 1 WHERE e.id = :id")
    int incrementCommentCount(@Param("id") Long id);
}
