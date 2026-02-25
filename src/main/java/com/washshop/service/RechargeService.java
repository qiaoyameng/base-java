package com.washshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.washshop.dto.RechargeDTO;
import com.washshop.entity.RechargeRecord;
import com.washshop.vo.Result;

import java.util.List;

public interface RechargeService extends IService<RechargeRecord> {

    Result<String> createRechargeOrder(Long userId, RechargeDTO dto);

    Result<Void> completeRecharge(String rechargeNo, String payNo);

    Result<List<RechargeRecord>> getRechargeRecords(Long userId);

    Result<RechargeRecord> getRechargeByNo(String rechargeNo);
}
