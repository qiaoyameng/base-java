package org.example.repository;

import org.example.entity.PhotoWall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoWallRepository extends JpaRepository<PhotoWall, Long> {

    List<PhotoWall> findByApprovedTrueAndDeletedFalseOrderBySortOrderAsc();

    Page<PhotoWall> findByApprovedTrueAndDeletedFalse(Pageable pageable);

    Page<PhotoWall> findByCustomerIdAndDeletedFalse(Long customerId, Pageable pageable);

    @Modifying
    @Query("UPDATE PhotoWall p SET p.likeCount = p.likeCount + 1 WHERE p.id = :id")
    int incrementLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE PhotoWall p SET p.approved = true WHERE p.id = :id")
    int approvePhoto(@Param("id") Long id);
}
