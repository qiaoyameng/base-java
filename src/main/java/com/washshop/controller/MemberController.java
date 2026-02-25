package com.washshop.controller;

import com.washshop.service.MemberService;
import com.washshop.vo.MemberVO;
import com.washshop.vo.PointsRecordVO;
import com.washshop.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/member")
@Tag(name = "会员管理", description = "会员等级、积分相关接口")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/info")
    @Operation(summary = "获取会员信息")
    public Result<MemberVO> getMemberInfo(@RequestAttribute("userId") Long userId) {
        return memberService.getMemberInfo(userId);
    }

    @GetMapping("/points-records")
    @Operation(summary = "获取积分记录")
    public Result<List<PointsRecordVO>> getPointsRecords(@RequestAttribute("userId") Long userId) {
        return memberService.getPointsRecords(userId);
    }
}
