package org.example.repository;

import org.example.entity.Activity;
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
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Page<Activity> findByEnabled(Boolean enabled, Pageable pageable);

    Page<Activity> findByTypeAndEnabled(Activity.ActivityType type, Boolean enabled, Pageable pageable);

    List<Activity> findByEnabledAndIsTopTrue(Boolean enabled);

    @Query("SELECT a FROM Activity a WHERE a.enabled = true " +
           "AND (a.startTime IS NULL OR a.startTime <= :now) " +
           "AND (a.endTime IS NULL OR a.endTime >= :now) " +
           "ORDER BY a.isTop DESC, a.createTime DESC")
    List<Activity> findActiveActivities(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE Activity a SET a.views = a.views + 1 WHERE a.id = :id")
    void incrementViews(@Param("id") Long id);

    List<Activity> findTop5ByEnabledOrderByCreateTimeDesc(Boolean enabled);
}
