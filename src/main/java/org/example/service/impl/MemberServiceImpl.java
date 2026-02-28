package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.entity.*;
import org.example.enums.MemberLevel;
import org.example.common.PageResult;
import org.example.repository.*;
import org.example.service.MemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final StoredValueTransactionRepository storedValueTransactionRepository;
    private final PointsTransactionRepository pointsTransactionRepository;
    private final MemberGiftRepository memberGiftRepository;

    private static final BigDecimal POINTS_EXCHANGE_RATE = new BigDecimal("0.01");

    @Override
    @Transactional
    public MemberDTO registerMember(MemberDTO memberDTO) {
        Member member = new Member();
        BeanUtils.copyProperties(memberDTO, member);
        member.setLevel(MemberLevel.NORMAL);
        member.setPoints(0);
        member.setTotalConsumption(BigDecimal.ZERO);
        member.setStoredValueBalance(BigDecimal.ZERO);
        member.setLoginCount(0);
        member.setActive(true);
        member.setDeleted(false);
        Member saved = memberRepository.save(member);
        return convertToMemberDTO(saved);
    }

    @Override
    @Transactional
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("会员不存在"));
        BeanUtils.copyProperties(memberDTO, member, "id", "phone", "password", "level", "points", "totalConsumption", "storedValueBalance", "loginCount");
        Member saved = memberRepository.save(member);
        return convertToMemberDTO(saved);
    }

    @Override
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("会员不存在"));
        return convertToMemberDTO(member);
    }

    @Override
    public MemberDTO getMemberByPhone(String phone) {
        Member member = memberRepository.findByPhoneAndDeletedFalse(phone)
                .orElseThrow(() -> new RuntimeException("会员不存在"));
        return convertToMemberDTO(member);
    }

    @Override
    public PageResult<MemberDTO> listMembers(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Member> page = memberRepository.findByDeletedFalse(pageable);
        List<MemberDTO> dtoList = page.getContent().stream()
                .map(this::convertToMemberDTO)
                .collect(Collectors.toList());
        Page<MemberDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(dtoList, pageable, page.getTotalElements());
        return PageResult.of(dtoPage);
    }

    @Override
    public PageResult<MemberDTO> listMembersByLevel(MemberLevel level, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Member> page = memberRepository.findByLevelAndDeletedFalse(level, pageable);
        List<MemberDTO> dtoList = page.getContent().stream()
                .map(this::convertToMemberDTO)
                .collect(Collectors.toList());
        Page<MemberDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(dtoList, pageable, page.getTotalElements());
        return PageResult.of(dtoPage);
    }

    @Override
    @Transactional
    public void addPoints(Long memberId, Integer points) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("会员不存在"));

        int multiplier = member.getLevel().getPointsMultiplier();
        int actualPoints = points * (100 + multiplier) / 100;

        int result = memberRepository.addPoints(memberId, actualPoints);
        if (result <= 0) {
            throw new RuntimeException("添加积分失败");
        }

        PointsTransaction transaction = new PointsTransaction();
        transaction.setMemberId(memberId);
        transaction.setMemberName(member.getName());
        transaction.setType(PointsTransaction.Type.EARN);
        transaction.setPoints(actualPoints);
        transaction.setBalanceBefore(member.getPoints());
        transaction.setBalanceAfter(member.getPoints() + actualPoints);
        transaction.setDescription("消费获得积分");
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setDeleted(false);
        pointsTransactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void deductPoints(Long memberId, Long points) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("会员不存在"));

        int result = memberRepository.deductPoints(memberId, points.intValue());
        if (result <= 0) {
            throw new RuntimeException("积分不足");
        }

        PointsTransaction transaction = new PointsTransaction();
        transaction.setMemberId(memberId);
        transaction.setMemberName(member.getName());
        transaction.setType(PointsTransaction.Type.DEDUCT);
        transaction.setPoints(points.intValue());
        transaction.setBalanceBefore(member.getPoints());
        transaction.setBalanceAfter(member.getPoints() - points.intValue());
        transaction.setDescription("订单抵扣积分");
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setDeleted(false);
        pointsTransactionRepository.save(transaction);
    }

    @Override
    public BigDecimal calculatePointsDiscount(Long memberId, Long points) {
        return POINTS_EXCHANGE_RATE.multiply(BigDecimal.valueOf(points));
    }

    @Override
    @Transactional
    public StoredValueTransactionDTO rechargeStoredValue(RechargeDTO rechargeDTO) {
        Member member = memberRepository.findById(rechargeDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("会员不存在"));

        BigDecimal totalAmount = rechargeDTO.getAmount();
        if (rechargeDTO.getGiftAmount() != null) {
            totalAmount = totalAmount.add(rechargeDTO.getGiftAmount());
        }

        BigDecimal balanceBefore = member.getStoredValueBalance();
        BigDecimal balanceAfter = balanceBefore.add(totalAmount);

        int result = memberRepository.addStoredValue(rechargeDTO.getMemberId(), totalAmount);
        if (result <= 0) {
            throw new RuntimeException("充值失败");
        }

        StoredValueTransaction transaction = new StoredValueTransaction();
        transaction.setMemberId(member.getId());
        transaction.setMemberName(member.getName());
        transaction.setType(StoredValueTransaction.Type.RECHARGE);
        transaction.setAmount(rechargeDTO.getAmount());
        transaction.setGiftAmount(rechargeDTO.getGiftAmount());
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setTransactionNo(generateTransactionNo());
        transaction.setRemark(rechargeDTO.getRemark());
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setDeleted(false);
        StoredValueTransaction saved = storedValueTransactionRepository.save(transaction);

        upgradeMemberLevel(member.getId());

        return convertToStoredValueTransactionDTO(saved);
    }

    @Override
    @Transactional
    public StoredValueTransactionDTO consumeStoredValue(Long memberId, BigDecimal amount, Long orderId, String orderNo) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("会员不存在"));

        if (member.getStoredValueBalance().compareTo(amount) < 0) {
            throw new RuntimeException("储值余额不足");
        }

        BigDecimal balanceBefore = member.getStoredValueBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(amount);

        int result = memberRepository.deductStoredValue(memberId, amount);
        if (result <= 0) {
            throw new RuntimeException("消费失败");
        }

        StoredValueTransaction transaction = new StoredValueTransaction();
        transaction.setMemberId(member.getId());
        transaction.setMemberName(member.getName());
        transaction.setType(StoredValueTransaction.Type.CONSUME);
        transaction.setAmount(amount);
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setTransactionNo(generateTransactionNo());
        transaction.setOrderId(orderId);
        transaction.setOrderNo(orderNo);
        transaction.setRemark("订单消费");
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setDeleted(false);
        StoredValueTransaction saved = storedValueTransactionRepository.save(transaction);

        return convertToStoredValueTransactionDTO(saved);
    }

    @Override
    public PageResult<StoredValueTransactionDTO> listStoredValueTransactions(Long memberId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<StoredValueTransaction> page = storedValueTransactionRepository.findByMemberIdAndDeletedFalse(memberId, pageable);
        List<StoredValueTransactionDTO> dtoList = page.getContent().stream()
                .map(this::convertToStoredValueTransactionDTO)
                .collect(Collectors.toList());
        Page<StoredValueTransactionDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(dtoList, pageable, page.getTotalElements());
        return PageResult.of(dtoPage);
    }

    @Override
    public PageResult<PointsTransactionDTO> listPointsTransactions(Long memberId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<PointsTransaction> page = pointsTransactionRepository.findByMemberIdAndDeletedFalse(memberId, pageable);
        List<PointsTransactionDTO> dtoList = page.getContent().stream()
                .map(this::convertToPointsTransactionDTO)
                .collect(Collectors.toList());
        Page<PointsTransactionDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(dtoList, pageable, page.getTotalElements());
        return PageResult.of(dtoPage);
    }

    @Override
    @Transactional
    public void upgradeMemberLevel(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("会员不存在"));

        MemberLevel newLevel = MemberLevel.getByConsumption(member.getTotalConsumption().doubleValue());
        if (newLevel.ordinal() > member.getLevel().ordinal()) {
            memberRepository.updateLevel(memberId, newLevel);
        }
    }

    @Override
    @Transactional
    public MemberGiftDTO createMemberGift(MemberGiftDTO dto) {
        MemberGift gift = new MemberGift();
        BeanUtils.copyProperties(dto, gift);
        gift.setDeleted(false);
        MemberGift saved = memberGiftRepository.save(gift);
        return convertToMemberGiftDTO(saved);
    }

    @Override
    public List<MemberGiftDTO> listMemberGifts() {
        return memberGiftRepository.findByActiveTrueAndDeletedFalseOrderBySortOrderAsc().stream()
                .map(this::convertToMemberGiftDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberGiftDTO> listMemberGiftsByLevel(MemberLevel level) {
        return memberGiftRepository.findByRequiredLevelAndActiveTrueAndDeletedFalseOrderBySortOrderAsc(level).stream()
                .map(this::convertToMemberGiftDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void claimGift(Long memberId, Long giftId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("会员不存在"));

        MemberGift gift = memberGiftRepository.findById(giftId)
                .orElseThrow(() -> new RuntimeException("赠品不存在"));

        if (!gift.getActive() || gift.getStock() <= 0) {
            throw new RuntimeException("赠品已领完或已下架");
        }

        if (member.getLevel().ordinal() < gift.getRequiredLevel().ordinal()) {
            throw new RuntimeException("会员等级不足");
        }

        if (gift.getRequiredPoints() != null && gift.getRequiredPoints() > 0) {
            if (member.getPoints() < gift.getRequiredPoints()) {
                throw new RuntimeException("积分不足");
            }
            deductPoints(memberId, gift.getRequiredPoints().longValue());
        }

        gift.setStock(gift.getStock() - 1);
        memberGiftRepository.save(gift);
    }

    private String generateTransactionNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return "SVT" + timestamp + random;
    }

    private MemberDTO convertToMemberDTO(Member member) {
        MemberDTO dto = new MemberDTO();
        BeanUtils.copyProperties(member, dto);
        return dto;
    }

    private StoredValueTransactionDTO convertToStoredValueTransactionDTO(StoredValueTransaction transaction) {
        StoredValueTransactionDTO dto = new StoredValueTransactionDTO();
        BeanUtils.copyProperties(transaction, dto);
        return dto;
    }

    private PointsTransactionDTO convertToPointsTransactionDTO(PointsTransaction transaction) {
        PointsTransactionDTO dto = new PointsTransactionDTO();
        BeanUtils.copyProperties(transaction, dto);
        return dto;
    }

    private MemberGiftDTO convertToMemberGiftDTO(MemberGift gift) {
        MemberGiftDTO dto = new MemberGiftDTO();
        BeanUtils.copyProperties(gift, dto);
        return dto;
    }
}
