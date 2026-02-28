package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Activity;
import org.example.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public Result<Activity> create(@RequestBody Activity activity) {
        return Result.success(activityService.createActivity(activity));
    }

    @GetMapping
    public Result<Page<Activity>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(activityService.findAll(page, size));
    }

    @GetMapping("/type/{type}")
    public Result<Page<Activity>> findByType(
            @PathVariable Activity.ActivityType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(activityService.findByType(type, page, size));
    }

    @GetMapping("/active")
    public Result<List<Activity>> findActiveActivities() {
        return Result.success(activityService.findActiveActivities());
    }

    @GetMapping("/top")
    public Result<List<Activity>> findTopActivities() {
        return Result.success(activityService.findTopActivities());
    }

    @GetMapping("/latest")
    public Result<List<Activity>> findLatest() {
        return Result.success(activityService.findLatest());
    }

    @GetMapping("/{id}")
    public Result<Activity> findById(@PathVariable Long id) {
        return Result.success(activityService.findById(id));
    }

    @PutMapping("/{id}")
    public Result<Activity> update(@PathVariable Long id, @RequestBody Activity activity) {
        return Result.success(activityService.updateActivity(id, activity));
    }

    @PostMapping("/{id}/view")
    public Result<Void> view(@PathVariable Long id) {
        activityService.view(id);
        return Result.success(null);
    }

    @PutMapping("/{id}/top")
    public Result<Void> toggleTop(@PathVariable Long id, @RequestParam Boolean isTop) {
        activityService.toggleTop(id, isTop);
        return Result.success(null);
    }

    @PutMapping("/{id}/enabled")
    public Result<Void> toggleEnabled(@PathVariable Long id, @RequestParam Boolean enabled) {
        activityService.toggleEnabled(id, enabled);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        activityService.delete(id);
        return Result.success(null);
    }
}
