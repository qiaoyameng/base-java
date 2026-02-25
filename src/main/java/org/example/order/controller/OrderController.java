package org.example.order.controller;

import org.example.common.Result;
import org.example.common.enums.OrderStatus;
import org.example.order.entity.Order;
import org.example.order.entity.OrderItem;
import org.example.order.entity.OrderStatusLog;
import org.example.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Result<List<Order>> list() {
        return Result.success(orderService.findAll());
    }

    @GetMapping("/page")
    public Result<Page<Order>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(orderService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(Result::success)
                .orElse(Result.error("订单不存在"));
    }

    @GetMapping("/no/{orderNo}")
    public Result<Order> getByOrderNo(@PathVariable String orderNo) {
        return orderService.findByOrderNo(orderNo)
                .map(Result::success)
                .orElse(Result.error("订单不存在"));
    }

    @GetMapping("/member/{memberId}")
    public Result<List<Order>> getByMember(@PathVariable Long memberId) {
        return Result.success(orderService.findByMemberId(memberId));
    }

    @GetMapping("/store/{storeId}")
    public Result<List<Order>> getByStore(@PathVariable Long storeId) {
        return Result.success(orderService.findByStoreId(storeId));
    }

    @GetMapping("/status/{status}")
    public Result<List<Order>> getByStatus(@PathVariable OrderStatus status) {
        return Result.success(orderService.findByStatus(status));
    }

    @GetMapping("/{id}/items")
    public Result<List<OrderItem>> getOrderItems(@PathVariable Long id) {
        return Result.success(orderService.getOrderItems(id));
    }

    @PostMapping
    public Result<Order> create(@RequestBody OrderCreateRequest request) {
        Order order = request.getOrder();
        List<OrderItem> items = request.getItems();
        return Result.success(orderService.createOrder(order, items));
    }

    @PostMapping("/{id}/pay")
    public Result<Void> pay(
            @PathVariable Long id,
            @RequestParam String paymentMethod,
            @RequestParam String paymentNo) {
        if (orderService.payOrder(id, paymentMethod, paymentNo)) {
            return Result.success();
        }
        return Result.error("支付失败，订单状态不正确");
    }

    @PostMapping("/{id}/accept")
    public Result<Void> accept(@PathVariable Long id, @RequestParam Long employeeId) {
        if (orderService.acceptOrder(id, employeeId)) {
            return Result.success();
        }
        return Result.error("接单失败，订单状态不正确");
    }

    @PostMapping("/{id}/complete")
    public Result<Void> complete(@PathVariable Long id) {
        if (orderService.completeService(id)) {
            return Result.success();
        }
        return Result.error("完成失败，订单状态不正确");
    }

    @PostMapping("/{id}/pickup")
    public Result<Void> pickup(@PathVariable Long id) {
        if (orderService.pickupOrder(id)) {
            return Result.success();
        }
        return Result.error("取件失败，订单状态不正确");
    }

    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(
            @PathVariable Long id,
            @RequestParam Long operatorId,
            @RequestParam String operatorType,
            @RequestParam String reason) {
        if (orderService.cancelOrder(id, operatorId, operatorType, reason)) {
            return Result.success();
        }
        return Result.error("取消失败，订单已完成或已取消");
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Order order) {
        if (orderService.updateOrder(id, order)) {
            return Result.success();
        }
        return Result.error("订单不存在");
    }

    @GetMapping("/{id}/status-logs")
    public Result<List<OrderStatusLog>> getStatusLogs(@PathVariable Long id) {
        return Result.success(orderService.getStatusLogs(id));
    }

    @GetMapping("/reminder")
    public Result<List<Order>> getOrdersToRemind() {
        return Result.success(orderService.findOrdersToRemind());
    }
}
