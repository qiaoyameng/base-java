package com.washshop.controller;

import com.washshop.dto.OrderCancelDTO;
import com.washshop.dto.OrderDTO;
import com.washshop.service.OrderService;
import com.washshop.vo.OrderVO;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Tag(name = "订单管理", description = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "创建订单")
    public Result<OrderVO> createOrder(@RequestAttribute("userId") Long userId, @RequestBody @Validated OrderDTO dto) {
        return orderService.createOrder(userId, dto);
    }

    @PostMapping("/{orderId}/pay")
    @Operation(summary = "支付订单")
    public Result<Void> payOrder(@PathVariable Long orderId, @RequestParam Integer payType) {
        return orderService.payOrder(orderId, payType);
    }

    @PostMapping("/{orderId}/accept")
    @Operation(summary = "接单")
    public Result<Void> acceptOrder(@PathVariable Long orderId) {
        return orderService.acceptOrder(orderId);
    }

    @PostMapping("/{orderId}/start-wash")
    @Operation(summary = "开始洗护")
    public Result<Void> startWash(@PathVariable Long orderId) {
        return orderService.startWash(orderId);
    }

    @PostMapping("/{orderId}/finish-wash")
    @Operation(summary = "完成洗护")
    public Result<Void> finishWash(@PathVariable Long orderId) {
        return orderService.finishWash(orderId);
    }

    @PostMapping("/{orderId}/ready")
    @Operation(summary = "待取件")
    public Result<Void> readyForPickup(@PathVariable Long orderId) {
        return orderService.readyForPickup(orderId);
    }

    @PostMapping("/{orderId}/deliver")
    @Operation(summary = "开始配送")
    public Result<Void> startDelivery(@PathVariable Long orderId) {
        return orderService.startDelivery(orderId);
    }

    @PostMapping("/{orderId}/complete")
    @Operation(summary = "完成订单")
    public Result<Void> completeOrder(@PathVariable Long orderId) {
        return orderService.completeOrder(orderId);
    }

    @PostMapping("/{orderId}/cancel")
    @Operation(summary = "取消订单")
    public Result<Void> cancelOrder(@RequestAttribute("userId") Long userId, @PathVariable Long orderId, @RequestBody @Validated OrderCancelDTO dto) {
        return orderService.cancelOrder(userId, orderId, dto);
    }

    @PostMapping("/{orderId}/refund")
    @Operation(summary = "申请退款")
    public Result<Void> applyRefund(@PathVariable Long orderId, @RequestParam String reason) {
        return orderService.applyRefund(orderId, reason);
    }

    @PostMapping("/{orderId}/refund/approve")
    @Operation(summary = "同意退款")
    public Result<Void> approveRefund(@PathVariable Long orderId) {
        return orderService.approveRefund(orderId);
    }

    @PostMapping("/{orderId}/refund/reject")
    @Operation(summary = "拒绝退款")
    public Result<Void> rejectRefund(@PathVariable Long orderId, @RequestParam String reason) {
        return orderService.rejectRefund(orderId, reason);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "获取订单详情")
    public Result<OrderVO> getOrderDetail(@PathVariable Long orderId) {
        return orderService.getOrderDetail(orderId);
    }

    @GetMapping("/my-orders")
    @Operation(summary = "获取我的订单列表")
    public Result<PageVO<OrderVO>> getMyOrders(@RequestAttribute("userId") Long userId,
                                               @RequestParam(required = false) Integer status,
                                               @RequestParam(defaultValue = "1") Long current,
                                               @RequestParam(defaultValue = "10") Long size) {
        return orderService.getUserOrders(userId, status, current, size);
    }

    @GetMapping("/store-orders")
    @Operation(summary = "获取门店订单列表")
    public Result<PageVO<OrderVO>> getStoreOrders(@RequestParam Long storeId,
                                                  @RequestParam(required = false) Integer status,
                                                  @RequestParam(defaultValue = "1") Long current,
                                                  @RequestParam(defaultValue = "10") Long size) {
        return orderService.getStoreOrders(storeId, status, current, size);
    }
}
