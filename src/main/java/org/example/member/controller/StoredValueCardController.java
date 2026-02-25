package org.example.member.controller;

import org.example.common.Result;
import org.example.member.entity.RechargeRecord;
import org.example.member.entity.StoredValueCard;
import org.example.member.service.StoredValueCardService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/stored-value-cards")
public class StoredValueCardController {
    
    private final StoredValueCardService cardService;

    public StoredValueCardController(StoredValueCardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/member/{memberId}")
    public Result<List<StoredValueCard>> getByMember(@PathVariable Long memberId) {
        return Result.success(cardService.findByMemberId(memberId));
    }

    @GetMapping("/{id}")
    public Result<StoredValueCard> getById(@PathVariable Long id) {
        return cardService.findById(id)
                .map(Result::success)
                .orElse(Result.error("储值卡不存在"));
    }

    @GetMapping("/card-no/{cardNo}")
    public Result<StoredValueCard> getByCardNo(@PathVariable String cardNo) {
        return cardService.findByCardNo(cardNo)
                .map(Result::success)
                .orElse(Result.error("储值卡不存在"));
    }

    @PostMapping("/create")
    public Result<StoredValueCard> createCard(@RequestParam Long memberId) {
        return Result.success(cardService.createCard(memberId));
    }

    @PostMapping("/{id}/recharge")
    public Result<Void> recharge(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false, defaultValue = "0") BigDecimal bonusAmount,
            @RequestParam String paymentMethod,
            @RequestParam String paymentNo) {
        if (cardService.recharge(id, amount, bonusAmount, paymentMethod, paymentNo)) {
            return Result.success();
        }
        return Result.error("储值卡不存在");
    }

    @PostMapping("/{id}/consume")
    public Result<Void> consume(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) Long orderId) {
        if (cardService.consume(id, amount, orderId)) {
            return Result.success();
        }
        return Result.error("余额不足或储值卡不存在");
    }

    @GetMapping("/member/{memberId}/recharge-records")
    public Result<List<RechargeRecord>> getRechargeRecords(@PathVariable Long memberId) {
        return Result.success(cardService.getRechargeRecords(memberId));
    }
}
