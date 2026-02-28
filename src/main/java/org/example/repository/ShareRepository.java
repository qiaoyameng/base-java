package org.example.repository;

import org.example.entity.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareRepository extends JpaRepository<Share, Long>, JpaSpecificationExecutor<Share> {

    Page<Share> findByEnabled(Boolean enabled, Pageable pageable);

    Page<Share> findByCustomerId(Long customerId, Pageable pageable);

    Page<Share> findByProductId(Long productId, Pageable pageable);

    List<Share> findByEnabledAndIsTopTrue(Boolean enabled);

    List<Share> findByTagsContainingAndEnabled(String tag, Boolean enabled);

    @Modifying
    @Query("UPDATE Share s SET s.likes = s.likes + 1 WHERE s.id = :id")
    void incrementLikes(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Share s SET s.views = s.views + 1 WHERE s.id = :id")
    void incrementViews(@Param("id") Long id);
}
