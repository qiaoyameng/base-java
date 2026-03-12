package org.example.repository;

import org.example.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Page<Announcement> findByDeletedFalseAndActiveTrueOrderByTopDescCreateTimeDesc(Pageable pageable);

    List<Announcement> findByDeletedFalseAndActiveTrueAndTopTrueOrderByCreateTimeDesc();

    Page<Announcement> findByDeletedFalseAndActiveTrueAndTypeOrderByTopDescCreateTimeDesc(String type, Pageable pageable);
}
