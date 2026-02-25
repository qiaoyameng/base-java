package org.example.member.controller;

import org.example.common.Result;
import org.example.common.enums.MemberLevel;
import org.example.member.entity.Member;
import org.example.member.entity.PointRecord;
import org.example.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public Result<List<Member>> list() {
        return Result.success(memberService.findAll());
    }

    @GetMapping("/page")
    public Result<Page<Member>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(memberService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Result<Member> getById(@PathVariable Long id) {
        return memberService.findById(id)
                .map(Result::success)
                .orElse(Result.error("会员不存在"));
    }

    @GetMapping("/user/{userId}")
    public Result<Member> getByUserId(@PathVariable Long userId) {
        return memberService.findByUserId(userId)
                .map(Result::success)
                .orElse(Result.error("会员不存在"));
    }

    @GetMapping("/phone/{phone}")
    public Result<Member> getByPhone(@PathVariable String phone) {
        return memberService.findByPhone(phone)
                .map(Result::success)
                .orElse(Result.error("会员不存在"));
    }

    @GetMapping("/level/{level}")
    public Result<List<Member>> getByLevel(@PathVariable MemberLevel level) {
        return Result.success(memberService.findByLevel(level));
    }

    @PostMapping
    public Result<Member> create(@RequestBody Member member) {
        if (memberService.findByPhone(member.getPhone()).isPresent()) {
            return Result.error("手机号已存在");
        }
        return Result.success(memberService.save(member));
    }

    @PostMapping("/register")
    public Result<Member> register(
            @RequestParam Long userId,
            @RequestParam String phone,
            @RequestParam(required = false) String memberName) {
        Member member = memberService.register(userId, phone, memberName);
        if (member == null) {
            return Result.error("手机号已注册");
        }
        return Result.success(member);
    }

    @PutMapping("/{id}")
    public Result<Member> update(@PathVariable Long id, @RequestBody Member member) {
        return memberService.findById(id).map(existing -> {
            existing.setMemberName(member.getMemberName());
            existing.setPhone(member.getPhone());
            return Result.success(memberService.save(existing));
        }).orElse(Result.error("会员不存在"));
    }

    @PostMapping("/{id}/points/add")
    public Result<Void> addPoints(
            @PathVariable Long id,
            @RequestParam Integer points,
            @RequestParam String type,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) String description) {
        if (memberService.addPoints(id, points, type, orderId, description)) {
            return Result.success();
        }
        return Result.error("会员不存在");
    }

    @PostMapping("/{id}/points/deduct")
    public Result<Void> deductPoints(
            @PathVariable Long id,
            @RequestParam Integer points,
            @RequestParam String type,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) String description) {
        if (memberService.deductPoints(id, points, type, orderId, description)) {
            return Result.success();
        }
        return Result.error("积分不足或会员不存在");
    }

    @PutMapping("/{id}/level")
    public Result<Void> updateLevel(@PathVariable Long id, @RequestParam MemberLevel level) {
        if (memberService.updateLevel(id, level)) {
            return Result.success();
        }
        return Result.error("会员不存在");
    }

    @GetMapping("/{id}/points/records")
    public Result<List<PointRecord>> getPointRecords(@PathVariable Long id) {
        return Result.success(memberService.getPointRecords(id));
    }
}
