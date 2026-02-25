package com.washshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.washshop.entity.Member;
import com.washshop.vo.MemberVO;
import com.washshop.vo.PointsRecordVO;
import com.washshop.vo.Result;

import java.math.BigDecimal;
import java.util.List;

public interface MemberService extends IService<Member> {

    Result<MemberVO> getMemberInfo(Long userId);

    Result<Void> createMember(Long userId);

    Result<Void> addPoints(Long userId, Integer points, String source, String description);

    Result<Void> deductPoints(Long userId, Integer points, String source, String description);

    Result<Void> addBalance(Long userId, BigDecimal amount);

    Result<Void> deductBalance(Long userId, BigDecimal amount);

    Result<Void> updateConsumption(Long userId, BigDecimal amount);

    Result<Void> upgradeLevel(Long userId);

    Result<List<PointsRecordVO>> getPointsRecords(Long userId);
}
