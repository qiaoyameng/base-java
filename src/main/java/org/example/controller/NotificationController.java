package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Notification;
import org.example.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/customer/{customerId}")
    public Result<Page<Notification>> findByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(notificationService.findByCustomerId(customerId, page, size));
    }

    @GetMapping("/customer/{customerId}/unread")
    public Result<List<Notification>> findUnread(@PathVariable Long customerId) {
        return Result.success(notificationService.findUnread(customerId));
    }

    @GetMapping("/customer/{customerId}/unread/count")
    public Result<Long> countUnread(@PathVariable Long customerId) {
        return Result.success(notificationService.countUnread(customerId));
    }

    @GetMapping("/{id}")
    public Result<Notification> findById(@PathVariable Long id) {
        return Result.success(notificationService.findById(id));
    }

    @PutMapping("/{id}/read")
    public Result<Notification> read(@PathVariable Long id) {
        return Result.success(notificationService.read(id));
    }

    @PutMapping("/customer/{customerId}/read-all")
    public Result<Void> readAll(@PathVariable Long customerId) {
        notificationService.readAll(customerId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return Result.success();
    }

    @DeleteMapping("/customer/{customerId}/read")
    public Result<Void> deleteRead(@PathVariable Long customerId) {
        notificationService.deleteRead(customerId);
        return Result.success();
    }
}
