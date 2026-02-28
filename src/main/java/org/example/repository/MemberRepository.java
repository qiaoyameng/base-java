package org.example.repository;

import org.example.entity.Member;
import org.example.enums.MemberLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByPhoneAndDeletedFalse(String phone);

    Page<Member> findByDeletedFalse(Pageable pageable);

    Page<Member> findByLevelAndDeletedFalse(MemberLevel level, Pageable pageable);

    @Modifying
    @Query("UPDATE Member m SET m.points = m.points + :points WHERE m.id = :memberId")
    int addPoints(@Param("memberId") Long memberId, @Param("points") Integer points);

    @Modifying
    @Query("UPDATE Member m SET m.points = m.points - :points WHERE m.id = :memberId AND m.points >= :points")
    int deductPoints(@Param("memberId") Long memberId, @Param("points") Integer points);

    @Modifying
    @Query("UPDATE Member m SET m.storedValueBalance = m.storedValueBalance + :amount WHERE m.id = :memberId")
    int addStoredValue(@Param("memberId") Long memberId, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("UPDATE Member m SET m.storedValueBalance = m.storedValueBalance - :amount WHERE m.id = :memberId AND m.storedValueBalance >= :amount")
    int deductStoredValue(@Param("memberId") Long memberId, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("UPDATE Member m SET m.totalConsumption = m.totalConsumption + :amount WHERE m.id = :memberId")
    int addConsumption(@Param("memberId") Long memberId, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("UPDATE Member m SET m.level = :level WHERE m.id = :memberId")
    int updateLevel(@Param("memberId") Long memberId, @Param("level") MemberLevel level);
}
