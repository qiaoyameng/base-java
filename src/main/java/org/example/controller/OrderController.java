package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.CreateOrderDTO;
import org.example.dto.OrderDTO;
import org.example.dto.OrderQueryDTO;
import org.example.common.PageResult;
import org.example.common.Result;
import org.example.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "订单管理", description = "订单创建、状态流转、退款审核等接口")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "创建订单", description = "顾客下单，支持选择支付方式和取货方式")
    public Result<OrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO) {
        return Result.success(orderService.createOrder(createOrderDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取订单详情", description = "根据ID获取订单详细信息")
    public Result<OrderDTO> getOrderById(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        return Result.success(orderService.getOrderById(id));
    }

    @GetMapping("/no/{orderNo}")
    @Operation(summary = "根据订单号获取订单", description = "根据订单编号查询订单")
    public Result<OrderDTO> getOrderByNo(
            @Parameter(description = "订单编号") @PathVariable String orderNo) {
        return Result.success(orderService.getOrderByNo(orderNo));
    }

    @GetMapping
    @Operation(summary = "查询订单列表", description = "支持分页和条件查询")
    public Result<PageResult<OrderDTO>> listOrders(OrderQueryDTO queryDTO) {
        return Result.success(orderService.listOrders(queryDTO));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "查询顾客订单", description = "查询指定顾客的所有订单")
    public Result<PageResult<OrderDTO>> listCustomerOrders(
            @Parameter(description = "顾客ID") @PathVariable Long customerId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(orderService.listCustomerOrders(customerId, pageNum, pageSize));
    }

    @PostMapping("/{id}/pay")
    @Operation(summary = "支付订单", description = "线上支付订单")
    public Result<Void> payOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        orderService.payOrder(id);
        return Result.success();
    }

    @PostMapping("/{id}/prepare")
    @Operation(summary = "备货完成", description = "订单备货完成，进入待取货/待发货状态")
    public Result<Void> prepareOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        orderService.prepareOrder(id);
        return Result.success();
    }

    @PostMapping("/{id}/ship")
    @Operation(summary = "发货", description = "订单发货，填写物流单号")
    public Result<Void> shipOrder(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "物流单号") @RequestParam String trackingNo) {
        orderService.shipOrder(id, trackingNo);
        return Result.success();
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "完成订单", description = "订单完成")
    public Result<Void> completeOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        orderService.completeOrder(id);
        return Result.success();
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "取消订单", description = "取消订单，恢复库存和积分")
    public Result<Void> cancelOrder(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "取消原因") @RequestParam String reason) {
        orderService.cancelOrder(id, reason);
        return Result.success();
    }

    @PostMapping("/{id}/refund/apply")
    @Operation(summary = "申请退款", description = "顾客申请退款")
    public Result<Void> applyRefund(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "退款原因") @RequestParam String reason) {
        orderService.applyRefund(id, reason);
        return Result.success();
    }

    @PostMapping("/{id}/refund/approve")
    @Operation(summary = "审核通过退款", description = "商家同意退款")
    public Result<Void> approveRefund(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        orderService.approveRefund(id);
        return Result.success();
    }

    @PostMapping("/{id}/refund/reject")
    @Operation(summary = "拒绝退款", description = "商家拒绝退款申请")
    public Result<Void> rejectRefund(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "拒绝原因") @RequestParam String reason) {
        orderService.rejectRefund(id, reason);
        return Result.success();
    }
}
