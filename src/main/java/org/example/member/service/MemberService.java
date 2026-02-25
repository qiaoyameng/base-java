package org.example.member.service;

import org.example.common.enums.MemberLevel;
import org.example.member.entity.Member;
import org.example.member.entity.PointRecord;
import org.example.member.repository.MemberRepository;
import org.example.member.repository.PointRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final PointRecordRepository pointRecordRepository;

    public MemberService(MemberRepository memberRepository, PointRecordRepository pointRecordRepository) {
        this.memberRepository = memberRepository;
        this.pointRecordRepository = pointRecordRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByUserId(Long userId) {
        return memberRepository.findByUserId(userId);
    }

    public Optional<Member> findByPhone(String phone) {
        return memberRepository.findByPhone(phone);
    }

    public List<Member> findByLevel(MemberLevel level) {
        return memberRepository.findByLevel(level);
    }

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public Member register(Long userId, String phone, String memberName) {
        if (memberRepository.findByPhone(phone).isPresent()) {
            return null;
        }
        Member member = new Member();
        member.setUserId(userId);
        member.setPhone(phone);
        member.setMemberName(memberName);
        member.setLevel(MemberLevel.NORMAL);
        member.setPoints(0);
        member.setTotalPoints(0);
        member.setBalance(BigDecimal.ZERO);
        member.setTotalSpent(BigDecimal.ZERO);
        return memberRepository.save(member);
    }

    @Transactional
    public boolean addPoints(Long memberId, Integer points, String type, Long orderId, String description) {
        return memberRepository.findById(memberId).map(member -> {
            member.setPoints(member.getPoints() + points);
            member.setTotalPoints(member.getTotalPoints() + points);
            memberRepository.save(member);
            
            PointRecord record = new PointRecord();
            record.setMemberId(memberId);
            record.setPoints(points);
            record.setType(type);
            record.setOrderId(orderId);
            record.setDescription(description);
            record.setBalanceAfter(member.getPoints());
            pointRecordRepository.save(record);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean deductPoints(Long memberId, Integer points, String type, Long orderId, String description) {
        return memberRepository.findById(memberId).map(member -> {
            if (member.getPoints() < points) {
                return false;
            }
            member.setPoints(member.getPoints() - points);
            memberRepository.save(member);
            
            PointRecord record = new PointRecord();
            record.setMemberId(memberId);
            record.setPoints(-points);
            record.setType(type);
            record.setOrderId(orderId);
            record.setDescription(description);
            record.setBalanceAfter(member.getPoints());
            pointRecordRepository.save(record);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean updateLevel(Long memberId, MemberLevel level) {
        return memberRepository.findById(memberId).map(member -> {
            member.setLevel(level);
            memberRepository.save(member);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean updateTotalSpent(Long memberId, BigDecimal amount) {
        return memberRepository.findById(memberId).map(member -> {
            member.setTotalSpent(member.getTotalSpent().add(amount));
            memberRepository.save(member);
            checkAndUpgradeLevel(member);
            return true;
        }).orElse(false);
    }

    private void checkAndUpgradeLevel(Member member) {
        BigDecimal totalSpent = member.getTotalSpent();
        if (totalSpent.compareTo(new BigDecimal("10000")) >= 0) {
            member.setLevel(MemberLevel.GOLD);
        } else if (totalSpent.compareTo(new BigDecimal("5000")) >= 0) {
            member.setLevel(MemberLevel.SILVER);
        }
        memberRepository.save(member);
    }

    public List<PointRecord> getPointRecords(Long memberId) {
        return pointRecordRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
    }
}
