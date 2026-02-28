package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.enums.MemberLevel;
import org.example.common.PageResult;
import org.example.common.Result;
import org.example.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "会员管理", description = "会员注册、等级、积分、储值卡等管理接口")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    @Operation(summary = "注册会员", description = "新会员注册")
    public Result<MemberDTO> registerMember(@Valid @RequestBody MemberDTO memberDTO) {
        return Result.success(memberService.registerMember(memberDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新会员信息", description = "更新会员基本信息")
    public Result<MemberDTO> updateMember(
            @Parameter(description = "会员ID") @PathVariable Long id,
            @Valid @RequestBody MemberDTO memberDTO) {
        return Result.success(memberService.updateMember(id, memberDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取会员详情", description = "根据ID获取会员详细信息")
    public Result<MemberDTO> getMemberById(
            @Parameter(description = "会员ID") @PathVariable Long id) {
        return Result.success(memberService.getMemberById(id));
    }

    @GetMapping("/phone/{phone}")
    @Operation(summary = "根据手机号查询会员", description = "根据手机号查询会员信息")
    public Result<MemberDTO> getMemberByPhone(
            @Parameter(description = "手机号") @PathVariable String phone) {
        return Result.success(memberService.getMemberByPhone(phone));
    }

    @GetMapping
    @Operation(summary = "查询会员列表", description = "分页查询所有会员")
    public Result<PageResult<MemberDTO>> listMembers(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(memberService.listMembers(pageNum, pageSize));
    }

    @GetMapping("/level/{level}")
    @Operation(summary = "按等级查询会员", description = "根据会员等级查询会员列表")
    public Result<PageResult<MemberDTO>> listMembersByLevel(
            @Parameter(description = "会员等级：NORMAL-普通，SILVER-银卡，GOLD-金卡") @PathVariable MemberLevel level,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(memberService.listMembersByLevel(level, pageNum, pageSize));
    }

    @PostMapping("/{id}/upgrade")
    @Operation(summary = "升级会员等级", description = "根据消费金额自动升级会员等级")
    public Result<Void> upgradeMemberLevel(
            @Parameter(description = "会员ID") @PathVariable Long id) {
        memberService.upgradeMemberLevel(id);
        return Result.success();
    }

    @PostMapping("/stored-value/recharge")
    @Operation(summary = "储值卡充值", description = "为会员储值卡充值，支持赠送金额")
    public Result<StoredValueTransactionDTO> rechargeStoredValue(@Valid @RequestBody RechargeDTO rechargeDTO) {
        return Result.success(memberService.rechargeStoredValue(rechargeDTO));
    }

    @GetMapping("/{memberId}/stored-value/transactions")
    @Operation(summary = "查询储值交易记录", description = "分页查询会员储值卡交易记录")
    public Result<PageResult<StoredValueTransactionDTO>> listStoredValueTransactions(
            @Parameter(description = "会员ID") @PathVariable Long memberId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(memberService.listStoredValueTransactions(memberId, pageNum, pageSize));
    }

    @GetMapping("/{memberId}/points/transactions")
    @Operation(summary = "查询积分记录", description = "分页查询会员积分变动记录")
    public Result<PageResult<PointsTransactionDTO>> listPointsTransactions(
            @Parameter(description = "会员ID") @PathVariable Long memberId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(memberService.listPointsTransactions(memberId, pageNum, pageSize));
    }

    @PostMapping("/gifts")
    @Operation(summary = "创建会员赠品", description = "创建会员等级专属赠品")
    public Result<MemberGiftDTO> createMemberGift(@Valid @RequestBody MemberGiftDTO dto) {
        return Result.success(memberService.createMemberGift(dto));
    }

    @GetMapping("/gifts")
    @Operation(summary = "查询所有赠品", description = "查询所有会员赠品")
    public Result<List<MemberGiftDTO>> listMemberGifts() {
        return Result.success(memberService.listMemberGifts());
    }

    @GetMapping("/gifts/level/{level}")
    @Operation(summary = "按等级查询赠品", description = "根据会员等级查询可领取的赠品")
    public Result<List<MemberGiftDTO>> listMemberGiftsByLevel(
            @Parameter(description = "会员等级") @PathVariable MemberLevel level) {
        return Result.success(memberService.listMemberGiftsByLevel(level));
    }

    @PostMapping("/{memberId}/gifts/{giftId}/claim")
    @Operation(summary = "领取赠品", description = "会员领取等级专属赠品")
    public Result<Void> claimGift(
            @Parameter(description = "会员ID") @PathVariable Long memberId,
            @Parameter(description = "赠品ID") @PathVariable Long giftId) {
        memberService.claimGift(memberId, giftId);
        return Result.success();
    }
}
