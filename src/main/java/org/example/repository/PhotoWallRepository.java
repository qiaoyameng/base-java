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

    Page<PhotoWall> findByEnabled(Boolean enabled, Pageable pageable);

    Page<PhotoWall> findByCustomerId(Long customerId, Pageable pageable);

    Page<PhotoWall> findByProductId(Long productId, Pageable pageable);

    List<PhotoWall> findByEnabledAndIsTopTrue(Boolean enabled);

    @Modifying
    @Query("UPDATE PhotoWall p SET p.likes = p.likes + 1 WHERE p.id = :id")
    void incrementLikes(@Param("id") Long id);
}
