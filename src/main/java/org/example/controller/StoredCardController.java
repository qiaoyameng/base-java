package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.StoredCardRequest;
import org.example.dto.StoredCardResponse;
import org.example.service.StoredCardService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "储值卡管理")
@RestController
@RequestMapping("/api/stored-cards")
@RequiredArgsConstructor
public class StoredCardController {
    private final StoredCardService storedCardService;

    @Operation(summary = "创建储值卡")
    @PostMapping
    public Result<StoredCardResponse> createCard(@Valid @RequestBody StoredCardRequest request) {
        return Result.success(storedCardService.createCard(request));
    }

    @Operation(summary = "储值卡充值")
    @PostMapping("/{cardId}/recharge")
    public Result<StoredCardResponse> recharge(
            @PathVariable Long cardId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {
        return Result.success(storedCardService.recharge(cardId, amount, description));
    }

    @Operation(summary = "储值卡扣费")
    @PostMapping("/{cardId}/deduct")
    public Result<StoredCardResponse> deduct(
            @PathVariable Long cardId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {
        return Result.success(storedCardService.deduct(cardId, amount, description));
    }

    @Operation(summary = "获取我的储值卡")
    @GetMapping("/my")
    public Result<List<StoredCardResponse>> getMyCards() {
        return Result.success(storedCardService.getCardsByCustomer(1L));
    }

    @Operation(summary = "删除储值卡")
    @DeleteMapping("/{cardId}")
    public Result<Void> deleteCard(@PathVariable Long cardId) {
        storedCardService.deleteCard(cardId);
        return Result.success();
    }
}
