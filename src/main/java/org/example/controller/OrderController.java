package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.common.PageResult;
import org.example.common.Result;
import org.example.dto.OrderDTO;
import org.example.dto.RefundDTO;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.OrderRefund;
import org.example.enums.OrderStatus;
import org.example.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Result<Order> createOrder(@Valid @RequestBody OrderDTO dto) {
        return Result.success(orderService.createOrder(dto));
    }

    @PostMapping("/{id}/pay")
    public Result<Order> payOrder(@PathVariable Long id) {
        return Result.success(orderService.payOrder(id));
    }

    @PostMapping("/{id}/prepare")
    public Result<Order> prepareOrder(@PathVariable Long id) {
        return Result.success(orderService.prepareOrder(id));
    }

    @PostMapping("/{id}/ship")
    public Result<Order> shipOrder(@PathVariable Long id) {
        return Result.success(orderService.shipOrder(id));
    }

    @PostMapping("/{id}/complete")
    public Result<Order> completeOrder(@PathVariable Long id) {
        return Result.success(orderService.completeOrder(id));
    }

    @PostMapping("/{id}/cancel")
    public Result<Order> cancelOrder(@PathVariable Long id) {
        return Result.success(orderService.cancelOrder(id));
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        List<OrderItem> items = orderService.getOrderItems(id);
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", items);
        return Result.success(result);
    }

    @GetMapping("/no/{orderNo}")
    public Result<Map<String, Object>> getOrderByNo(@PathVariable String orderNo) {
        Order order = orderService.getOrderByOrderNo(orderNo);
        List<OrderItem> items = orderService.getOrderItems(order.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", items);
        return Result.success(result);
    }

    @GetMapping
    public Result<PageResult<Order>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Order> orders = orderService.getOrders(pageRequest);
        return Result.success(PageResult.of(orders));
    }

    @GetMapping("/member/{memberId}")
    public Result<PageResult<Order>> getMemberOrders(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Order> orders = orderService.getOrdersByMember(memberId, pageRequest);
        return Result.success(PageResult.of(orders));
    }

    @GetMapping("/status/{status}")
    public Result<PageResult<Order>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Order> orders = orderService.getOrdersByStatus(status, pageRequest);
        return Result.success(PageResult.of(orders));
    }

    @PostMapping("/{id}/refund")
    public Result<OrderRefund> applyRefund(@PathVariable Long id, @Valid @RequestBody RefundDTO dto) {
        return Result.success(orderService.applyRefund(id, dto));
    }

    @PostMapping("/refund/{refundId}/approve")
    public Result<OrderRefund> approveRefund(
            @PathVariable Long refundId,
            @RequestParam Long reviewerId,
            @RequestParam(required = false) String remark) {
        return Result.success(orderService.approveRefund(refundId, reviewerId, remark));
    }

    @PostMapping("/refund/{refundId}/reject")
    public Result<OrderRefund> rejectRefund(
            @PathVariable Long refundId,
            @RequestParam Long reviewerId,
            @RequestParam(required = false) String remark) {
        return Result.success(orderService.rejectRefund(refundId, reviewerId, remark));
    }

    @GetMapping("/refunds")
    public Result<PageResult<OrderRefund>> getRefunds(
            @RequestParam(defaultValue = "PENDING") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<OrderRefund> refunds = orderService.getRefundsByStatus(status, pageRequest);
        return Result.success(PageResult.of(refunds));
    }

    @GetMapping("/member/{memberId}/refunds")
    public Result<PageResult<OrderRefund>> getMemberRefunds(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<OrderRefund> refunds = orderService.getMemberRefunds(memberId, pageRequest);
        return Result.success(PageResult.of(refunds));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return Result.success();
    }
}
