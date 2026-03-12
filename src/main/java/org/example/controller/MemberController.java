package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.PageResult;
import org.example.common.Result;
import org.example.dto.MemberDTO;
import org.example.dto.RechargeDTO;
import org.example.entity.Member;
import org.example.entity.MemberBalanceLog;
import org.example.entity.MemberPointsLog;
import org.example.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public Result<Member> createMember(@Valid @RequestBody MemberDTO dto) {
        return Result.success(memberService.createMember(dto));
    }

    @PutMapping("/{id}")
    public Result<Member> updateMember(@PathVariable Long id, @Valid @RequestBody MemberDTO dto) {
        return Result.success(memberService.updateMember(id, dto));
    }

    @GetMapping("/{id}")
    public Result<Member> getMember(@PathVariable Long id) {
        return Result.success(memberService.getMemberById(id));
    }

    @GetMapping("/phone/{phone}")
    public Result<Member> getMemberByPhone(@PathVariable String phone) {
        return Result.success(memberService.getMemberByPhone(phone));
    }

    @GetMapping
    public Result<PageResult<Member>> getMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Member> members = memberService.getMembers(pageRequest);
        return Result.success(PageResult.of(members));
    }

    @GetMapping("/search")
    public Result<PageResult<Member>> searchMembers(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Member> members = memberService.searchMembers(name, pageRequest);
        return Result.success(PageResult.of(members));
    }

    @PostMapping("/{id}/recharge")
    public Result<Member> recharge(@PathVariable Long id, @Valid @RequestBody RechargeDTO dto) {
        return Result.success(memberService.recharge(id, dto));
    }

    @PostMapping("/{id}/points/deduct")
    public Result<Void> deductPoints(
            @PathVariable Long id,
            @RequestParam Integer points,
            @RequestParam String remark) {
        memberService.deductPoints(id, points, remark);
        return Result.success();
    }

    @GetMapping("/{id}/balance-logs")
    public Result<PageResult<MemberBalanceLog>> getBalanceLogs(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<MemberBalanceLog> logs = memberService.getBalanceLogs(id, pageRequest);
        return Result.success(PageResult.of(logs));
    }

    @GetMapping("/{id}/points-logs")
    public Result<PageResult<MemberPointsLog>> getPointsLogs(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<MemberPointsLog> logs = memberService.getPointsLogs(id, pageRequest);
        return Result.success(PageResult.of(logs));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return Result.success();
    }
}
