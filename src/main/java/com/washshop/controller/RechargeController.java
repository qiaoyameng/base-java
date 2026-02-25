package com.washshop.controller;

import com.washshop.dto.RechargeDTO;
import com.washshop.entity.RechargeRecord;
import com.washshop.service.RechargeService;
import com.washshop.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recharge")
@Tag(name = "储值卡管理", description = "储值卡充值相关接口")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @PostMapping
    @Operation(summary = "创建充值订单")
    public Result<String> createRechargeOrder(@RequestAttribute("userId") Long userId,
                                              @RequestBody @Validated RechargeDTO dto) {
        return rechargeService.createRechargeOrder(userId, dto);
    }

    @PostMapping("/complete")
    @Operation(summary = "完成充值")
    public Result<Void> completeRecharge(@RequestParam String rechargeNo,
                                         @RequestParam String payNo) {
        return rechargeService.completeRecharge(rechargeNo, payNo);
    }

    @GetMapping("/records")
    @Operation(summary = "获取充值记录")
    public Result<List<RechargeRecord>> getRechargeRecords(@RequestAttribute("userId") Long userId) {
        return rechargeService.getRechargeRecords(userId);
    }

    @GetMapping("/{rechargeNo}")
    @Operation(summary = "查询充值订单")
    public Result<RechargeRecord> getRechargeByNo(@PathVariable String rechargeNo) {
        return rechargeService.getRechargeByNo(rechargeNo);
    }
}
