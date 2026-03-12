package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.MemberDTO;
import org.example.dto.RechargeDTO;
import org.example.entity.Member;
import org.example.entity.MemberBalanceLog;
import org.example.entity.MemberPointsLog;
import org.example.enums.MemberLevel;
import org.example.exception.BusinessException;
import org.example.repository.MemberBalanceLogRepository;
import org.example.repository.MemberPointsLogRepository;
import org.example.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberBalanceLogRepository balanceLogRepository;
    private final MemberPointsLogRepository pointsLogRepository;

    public Member createMember(MemberDTO dto) {
        if (memberRepository.existsByPhoneAndDeletedFalse(dto.getPhone())) {
            throw new BusinessException("该手机号已注册");
        }
        Member member = new Member();
        member.setPhone(dto.getPhone());
        member.setName(dto.getName());
        member.setAvatar(dto.getAvatar());
        return memberRepository.save(member);
    }

    public Member updateMember(Long id, MemberDTO dto) {
        Member member = getMemberById(id);
        if (!member.getPhone().equals(dto.getPhone()) && 
            memberRepository.existsByPhoneAndDeletedFalse(dto.getPhone())) {
            throw new BusinessException("该手机号已被使用");
        }
        member.setPhone(dto.getPhone());
        member.setName(dto.getName());
        member.setAvatar(dto.getAvatar());
        return memberRepository.save(member);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("会员不存在"));
    }

    public Member getMemberByPhone(String phone) {
        return memberRepository.findByPhoneAndDeletedFalse(phone)
                .orElseThrow(() -> new BusinessException("会员不存在"));
    }

    public Page<Member> getMembers(Pageable pageable) {
        return memberRepository.findByDeletedFalse(pageable);
    }

    public Page<Member> searchMembers(String name, Pageable pageable) {
        return memberRepository.findByNameContainingAndDeletedFalse(name, pageable);
    }

    @Transactional
    public Member recharge(Long memberId, RechargeDTO dto) {
        Member member = getMemberById(memberId);
        BigDecimal newBalance = member.getBalance().add(dto.getAmount());
        member.setBalance(newBalance);
        memberRepository.save(member);

        MemberBalanceLog log = new MemberBalanceLog();
        log.setMemberId(memberId);
        log.setAmount(dto.getAmount());
        log.setBalanceAfter(newBalance);
        log.setType("RECHARGE");
        log.setRemark(dto.getRemark());
        balanceLogRepository.save(log);

        return member;
    }

    @Transactional
    public void consumeBalance(Long memberId, BigDecimal amount, Long orderId) {
        Member member = getMemberById(memberId);
        if (member.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }
        BigDecimal newBalance = member.getBalance().subtract(amount);
        member.setBalance(newBalance);
        memberRepository.save(member);

        MemberBalanceLog log = new MemberBalanceLog();
        log.setMemberId(memberId);
        log.setAmount(amount.negate());
        log.setBalanceAfter(newBalance);
        log.setType("CONSUME");
        log.setRemark("订单消费");
        log.setRelatedOrderId(orderId);
        balanceLogRepository.save(log);
    }

    @Transactional
    public void addPoints(Long memberId, Integer points, Long orderId) {
        Member member = getMemberById(memberId);
        int newPoints = member.getPoints() + points;
        member.setPoints(newPoints);
        member.setLevel(MemberLevel.getLevelByPoints(newPoints));
        memberRepository.save(member);

        MemberPointsLog log = new MemberPointsLog();
        log.setMemberId(memberId);
        log.setPoints(points);
        log.setPointsAfter(newPoints);
        log.setType("EARN");
        log.setRemark("消费获得积分");
        log.setRelatedOrderId(orderId);
        pointsLogRepository.save(log);
    }

    @Transactional
    public void deductPoints(Long memberId, Integer points, String remark) {
        Member member = getMemberById(memberId);
        if (member.getPoints() < points) {
            throw new BusinessException("积分不足");
        }
        int newPoints = member.getPoints() - points;
        member.setPoints(newPoints);
        member.setLevel(MemberLevel.getLevelByPoints(newPoints));
        memberRepository.save(member);

        MemberPointsLog log = new MemberPointsLog();
        log.setMemberId(memberId);
        log.setPoints(-points);
        log.setPointsAfter(newPoints);
        log.setType("DEDUCT");
        log.setRemark(remark);
        pointsLogRepository.save(log);
    }

    @Transactional
    public void updateTotalSpent(Long memberId, BigDecimal amount) {
        Member member = getMemberById(memberId);
        member.setTotalSpent(member.getTotalSpent().add(amount));
        member.setOrderCount(member.getOrderCount() + 1);
        memberRepository.save(member);
    }

    public Page<MemberBalanceLog> getBalanceLogs(Long memberId, Pageable pageable) {
        return balanceLogRepository.findByMemberIdOrderByCreateTimeDesc(memberId, pageable);
    }

    public Page<MemberPointsLog> getPointsLogs(Long memberId, Pageable pageable) {
        return pointsLogRepository.findByMemberIdOrderByCreateTimeDesc(memberId, pageable);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        member.setDeleted(true);
        memberRepository.save(member);
    }
}
