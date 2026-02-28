package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.dto.OrderRequest;
import org.example.entity.Orders;
import org.example.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Result<Orders> createOrder(@RequestBody OrderRequest request) {
        try {
            return Result.success(orderService.createOrder(request));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{orderNo}")
    public Result<Orders> findByOrderNo(@PathVariable String orderNo) {
        Orders order = orderService.findByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }

    @GetMapping("/id/{id}")
    public Result<Orders> findById(@PathVariable Long id) {
        Orders order = orderService.findById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }

    @GetMapping("/customer/{customerId}")
    public Result<Page<Orders>> findByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(orderService.findByCustomerId(customerId, page, size));
    }

    @GetMapping("/customer/{customerId}/status")
    public Result<Page<Orders>> findByCustomerIdAndStatus(
            @PathVariable Long customerId,
            @RequestParam Orders.OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(orderService.findByCustomerIdAndStatus(customerId, status, page, size));
    }

    @GetMapping
    public Result<Page<Orders>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(orderService.findAll(page, size));
    }

    @PostMapping("/{orderNo}/payment")
    public Result<Orders> payment(
            @PathVariable String orderNo,
            @RequestParam Orders.PaymentMethod paymentMethod) {
        try {
            return Result.success(orderService.payment(orderNo, paymentMethod));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/prepare-complete")
    public Result<Orders> prepareComplete(@PathVariable String orderNo) {
        try {
            return Result.success(orderService.prepareComplete(orderNo));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/delivery")
    public Result<Orders> delivery(@PathVariable String orderNo) {
        try {
            return Result.success(orderService.delivery(orderNo));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/complete")
    public Result<Orders> complete(@PathVariable String orderNo) {
        try {
            return Result.success(orderService.complete(orderNo));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<Orders> cancel(
            @PathVariable String orderNo,
            @RequestParam String reason) {
        try {
            return Result.success(orderService.cancel(orderNo, reason));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/refund/apply")
    public Result<Orders> applyRefund(
            @PathVariable String orderNo,
            @RequestParam String reason) {
        try {
            return Result.success(orderService.applyRefund(orderNo, reason));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/refund/approve")
    public Result<Orders> approveRefund(
            @PathVariable String orderNo,
            @RequestParam Boolean approved) {
        try {
            return Result.success(orderService.approveRefund(orderNo, approved));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
