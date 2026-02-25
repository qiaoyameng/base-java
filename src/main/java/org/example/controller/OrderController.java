package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.OrderRequest;
import org.example.dto.OrderResponse;
import org.example.enums.OrderStatus;
import org.example.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "订单管理", description = "订单创建、支付、状态流转等接口")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "创建订单")
    public Result<OrderResponse> createOrder(
            @RequestParam Long customerId,
            @RequestBody OrderRequest request) {
        return Result.success(orderService.createOrder(customerId, request));
    }

    @PutMapping("/{orderId}/pay")
    @Operation(summary = "订单支付")
    public Result<OrderResponse> payOrder(@PathVariable Long orderId) {
        return Result.success(orderService.payOrder(orderId));
    }

    @PutMapping("/{orderId}/accept")
    @Operation(summary = "门店接单")
    public Result<OrderResponse> acceptOrder(@PathVariable Long orderId) {
        return Result.success(orderService.acceptOrder(orderId));
    }

    @PutMapping("/{orderId}/start")
    @Operation(summary = "开始洗护")
    public Result<OrderResponse> startWash(@PathVariable Long orderId) {
        return Result.success(orderService.startWash(orderId));
    }

    @PutMapping("/{orderId}/complete")
    @Operation(summary = "完成洗护")
    public Result<OrderResponse> completeOrder(@PathVariable Long orderId) {
        return Result.success(orderService.completeOrder(orderId));
    }

    @PutMapping("/{orderId}/pickup")
    @Operation(summary = "取件")
    public Result<OrderResponse> pickupOrder(@PathVariable Long orderId) {
        return Result.success(orderService.pickupOrder(orderId));
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "取消订单")
    public Result<OrderResponse> cancelOrder(@PathVariable Long orderId) {
        return Result.success(orderService.cancelOrder(orderId));
    }

    @PostMapping("/{orderId}/refund")
    @Operation(summary = "申请退款")
    public Result<OrderResponse> requestRefund(
            @PathVariable Long orderId,
            @RequestParam String reason) {
        return Result.success(orderService.requestRefund(orderId, reason));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "根据ID查询订单")
    public Result<OrderResponse> getOrderById(@PathVariable Long orderId) {
        return Result.success(orderService.getOrderById(orderId));
    }

    @GetMapping("/no/{orderNo}")
    @Operation(summary = "根据订单号查询")
    public Result<OrderResponse> getOrderByNo(@PathVariable String orderNo) {
        return Result.success(orderService.getOrderByNo(orderNo));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "查询客户的所有订单")
    public Result<List<OrderResponse>> getOrdersByCustomer(@PathVariable Long customerId) {
        return Result.success(orderService.getOrdersByCustomer(customerId));
    }

    @GetMapping("/customer/{customerId}/status/{status}")
    @Operation(summary = "按状态查询客户订单")
    public Result<List<OrderResponse>> getOrdersByCustomerAndStatus(
            @PathVariable Long customerId,
            @PathVariable OrderStatus status) {
        return Result.success(orderService.getOrdersByCustomerAndStatus(customerId, status));
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "查询门店订单")
    public Result<List<OrderResponse>> getOrdersByStore(@PathVariable Long storeId) {
        return Result.success(orderService.getOrdersByStore(storeId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "按状态查询所有订单")
    public Result<List<OrderResponse>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return Result.success(orderService.getOrdersByStatus(status));
    }
}
