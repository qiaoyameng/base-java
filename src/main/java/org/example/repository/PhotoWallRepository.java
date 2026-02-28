package org.example.repository;

import org.example.entity.PhotoWall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhotoWallRepository extends JpaRepository<PhotoWall, Long> {
    Page<PhotoWall> findByDeletedFalseAndApprovedTrueOrderByCreateTimeDesc(Pageable pageable);

    Page<PhotoWall> findByMemberIdAndDeletedFalse(Long memberId, Pageable pageable);

    Page<PhotoWall> findByProductIdAndDeletedFalseAndApprovedTrue(Long productId, Pageable pageable);

    @Query("SELECT p FROM PhotoWall p WHERE p.deleted = false AND p.approved = true ORDER BY p.likes DESC")
    List<PhotoWall> findPopularPhotos(Pageable pageable);
}
