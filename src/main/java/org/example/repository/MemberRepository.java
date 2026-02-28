package org.example.repository;

import org.example.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndDeletedFalse(Long id);

    Optional<Member> findByPhoneAndDeletedFalse(String phone);

    Page<Member> findByDeletedFalse(Pageable pageable);

    @Query("SELECT m FROM Member m WHERE m.deleted = false AND m.name LIKE %:name%")
    Page<Member> findByNameContainingAndDeletedFalse(@Param("name") String name, Pageable pageable);

    boolean existsByPhoneAndDeletedFalse(String phone);
}
