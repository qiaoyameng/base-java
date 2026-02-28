package org.example.repository;

import org.example.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findByActiveTrueAndDisplayOnHomeTrueAndStartTimeBeforeAndEndTimeAfterOrderBySortOrderAsc(
            LocalDateTime now1, LocalDateTime now2);

    Page<Announcement> findByActiveTrueAndStartTimeBeforeAndEndTimeAfterOrderByCreateTimeDesc(
            LocalDateTime now1, LocalDateTime now2, Pageable pageable);

    @Modifying
    @Query("UPDATE Announcement a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    int incrementViewCount(@Param("id") Long id);
}
