package com.washshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.washshop.dto.RechargeDTO;
import com.washshop.entity.RechargeRecord;
import com.washshop.mapper.RechargeRecordMapper;
import com.washshop.service.MemberService;
import com.washshop.service.RechargeService;
import com.washshop.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RechargeServiceImpl extends ServiceImpl<RechargeRecordMapper, RechargeRecord> implements RechargeService {

    @Autowired
    private MemberService memberService;

    private static final DateTimeFormatter RECHARGE_NO_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> createRechargeOrder(Long userId, RechargeDTO dto) {
        RechargeRecord record = new RechargeRecord();
        record.setUserId(userId);
        record.setRechargeNo(generateRechargeNo());
        record.setAmount(dto.getAmount());

        BigDecimal giftAmount = calculateGiftAmount(dto.getAmount());
        record.setGiftAmount(giftAmount);

        record.setPayType(dto.getPayType());
        record.setStatus(0);

        save(record);
        return Result.success(record.getRechargeNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> completeRecharge(String rechargeNo, String payNo) {
        RechargeRecord record = baseMapper.selectByRechargeNo(rechargeNo);
        if (record == null) {
            return Result.error("充值记录不存在");
        }
        if (record.getStatus() != 0) {
            return Result.error("充值状态不正确");
        }

        record.setStatus(1);
        record.setPayNo(payNo);
        record.setPayTime(LocalDateTime.now());
        updateById(record);

        BigDecimal totalAmount = record.getAmount().add(record.getGiftAmount());
        memberService.addBalance(record.getUserId(), totalAmount);

        return Result.success();
    }

    @Override
    public Result<List<RechargeRecord>> getRechargeRecords(Long userId) {
        List<RechargeRecord> records = baseMapper.selectByUserId(userId);
        return Result.success(records);
    }

    @Override
    public Result<RechargeRecord> getRechargeByNo(String rechargeNo) {
        RechargeRecord record = baseMapper.selectByRechargeNo(rechargeNo);
        if (record == null) {
            return Result.error("充值记录不存在");
        }
        return Result.success(record);
    }

    private String generateRechargeNo() {
        return "RC" + LocalDateTime.now().format(RECHARGE_NO_FORMATTER) + String.format("%04d", (int)(Math.random() * 10000));
    }

    private BigDecimal calculateGiftAmount(BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("500")) >= 0) {
            return amount.multiply(new BigDecimal("0.1"));
        } else if (amount.compareTo(new BigDecimal("200")) >= 0) {
            return amount.multiply(new BigDecimal("0.05"));
        }
        return BigDecimal.ZERO;
    }
}
