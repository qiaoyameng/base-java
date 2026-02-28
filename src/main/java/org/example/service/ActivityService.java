package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Activity;
import org.example.repository.ActivityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public Activity createActivity(Activity activity) {
        activity.setViews(0);
        activity.setIsTop(false);
        activity.setEnabled(true);
        return activityRepository.save(activity);
    }

    public Page<Activity> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "isTop", "createTime"));
        return activityRepository.findByEnabled(true, pageable);
    }

    public Page<Activity> findByType(Activity.ActivityType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "isTop", "createTime"));
        return activityRepository.findByTypeAndEnabled(type, true, pageable);
    }

    public List<Activity> findActiveActivities() {
        return activityRepository.findActiveActivities(LocalDateTime.now());
    }

    public List<Activity> findTopActivities() {
        return activityRepository.findByEnabledAndIsTopTrue(true);
    }

    public List<Activity> findLatest() {
        return activityRepository.findTop5ByEnabledOrderByCreateTimeDesc(true);
    }

    public Activity findById(Long id) {
        return activityRepository.findById(id).orElse(null);
    }

    @Transactional
    public Activity updateActivity(Long id, Activity update) {
        Activity activity = findById(id);
        if (activity != null) {
            if (update.getTitle() != null) activity.setTitle(update.getTitle());
            if (update.getContent() != null) activity.setContent(update.getContent());
            if (update.getSummary() != null) activity.setSummary(update.getSummary());
            if (update.getCoverImage() != null) activity.setCoverImage(update.getCoverImage());
            if (update.getImages() != null) activity.setImages(update.getImages());
            if (update.getType() != null) activity.setType(update.getType());
            if (update.getStartTime() != null) activity.setStartTime(update.getStartTime());
            if (update.getEndTime() != null) activity.setEndTime(update.getEndTime());
            return activityRepository.save(activity);
        }
        return null;
    }

    @Transactional
    public void view(Long id) {
        activityRepository.incrementViews(id);
    }

    @Transactional
    public void toggleTop(Long id, Boolean isTop) {
        Activity activity = findById(id);
        if (activity != null) {
            activity.setIsTop(isTop);
            activityRepository.save(activity);
        }
    }

    @Transactional
    public void toggleEnabled(Long id, Boolean enabled) {
        Activity activity = findById(id);
        if (activity != null) {
            activity.setEnabled(enabled);
            activityRepository.save(activity);
        }
    }

    @Transactional
    public void delete(Long id) {
        activityRepository.deleteById(id);
    }
}
