package com.washshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.washshop.entity.Member;
import com.washshop.entity.PointsRecord;
import com.washshop.enums.MemberLevel;
import com.washshop.mapper.MemberMapper;
import com.washshop.mapper.PointsRecordMapper;
import com.washshop.service.MemberService;
import com.washshop.vo.MemberVO;
import com.washshop.vo.PointsRecordVO;
import com.washshop.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    @Override
    public Result<MemberVO> getMemberInfo(Long userId) {
        Member member = baseMapper.selectByUserId(userId);
        if (member == null) {
            createMember(userId);
            member = baseMapper.selectByUserId(userId);
        }
        return Result.success(convertToVO(member));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> createMember(Long userId) {
        Member member = new Member();
        member.setUserId(userId);
        member.setLevel(MemberLevel.NORMAL.getCode());
        member.setPoints(0);
        member.setBalance(BigDecimal.ZERO);
        member.setTotalConsumption(BigDecimal.ZERO);
        member.setTotalOrders(0);
        save(member);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> addPoints(Long userId, Integer points, String source, String description) {
        int rows = baseMapper.addPoints(userId, points);
        if (rows == 0) {
            return Result.error("添加积分失败");
        }

        Member member = baseMapper.selectByUserId(userId);

        PointsRecord record = new PointsRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setType(1);
        record.setSource(source);
        record.setDescription(description);
        record.setBalance(member.getPoints());
        pointsRecordMapper.insert(record);

        upgradeLevel(userId);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deductPoints(Long userId, Integer points, String source, String description) {
        int rows = baseMapper.deductPoints(userId, points);
        if (rows == 0) {
            return Result.error("积分不足");
        }

        Member member = baseMapper.selectByUserId(userId);

        PointsRecord record = new PointsRecord();
        record.setUserId(userId);
        record.setPoints(-points);
        record.setType(2);
        record.setSource(source);
        record.setDescription(description);
        record.setBalance(member.getPoints());
        pointsRecordMapper.insert(record);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> addBalance(Long userId, BigDecimal amount) {
        int rows = baseMapper.addBalance(userId, amount);
        if (rows == 0) {
            return Result.error("添加余额失败");
        }
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deductBalance(Long userId, BigDecimal amount) {
        int rows = baseMapper.deductBalance(userId, amount);
        if (rows == 0) {
            return Result.error("余额不足");
        }
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateConsumption(Long userId, BigDecimal amount) {
        Member member = baseMapper.selectByUserId(userId);
        if (member == null) {
            createMember(userId);
            member = baseMapper.selectByUserId(userId);
        }

        member.setTotalConsumption(member.getTotalConsumption().add(amount));
        member.setTotalOrders(member.getTotalOrders() + 1);
        updateById(member);

        int points = amount.intValue();
        addPoints(userId, points, "ORDER", "消费获得积分");

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> upgradeLevel(Long userId) {
        Member member = baseMapper.selectByUserId(userId);
        if (member == null) {
            return Result.error("会员不存在");
        }

        MemberLevel newLevel = MemberLevel.fromPoints(member.getPoints());
        if (newLevel.getCode() > member.getLevel()) {
            member.setLevel(newLevel.getCode());
            member.setUpgradeTime(LocalDateTime.now());
            updateById(member);
        }

        return Result.success();
    }

    @Override
    public Result<List<PointsRecordVO>> getPointsRecords(Long userId) {
        List<PointsRecord> records = pointsRecordMapper.selectByUserId(userId);
        List<PointsRecordVO> voList = records.stream().map(record -> {
            PointsRecordVO vo = new PointsRecordVO();
            BeanUtils.copyProperties(record, vo);
            vo.setType(record.getType() == 1 ? "获得" : "使用");
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    private MemberVO convertToVO(Member member) {
        MemberVO vo = new MemberVO();
        BeanUtils.copyProperties(member, vo);

        MemberLevel level = MemberLevel.fromCode(member.getLevel());
        vo.setLevelName(level.getDesc());

        return vo;
    }
}
