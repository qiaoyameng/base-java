package org.example.record.service;

import org.example.record.entity.ServiceReview;
import org.example.record.repository.ServiceReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceReviewService {
    
    private final ServiceReviewRepository reviewRepository;

    public ServiceReviewService(ServiceReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ServiceReview> findAll() {
        return reviewRepository.findAll();
    }

    public Page<ServiceReview> findAll(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public Optional<ServiceReview> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<ServiceReview> findByOrderId(Long orderId) {
        return reviewRepository.findByOrderId(orderId);
    }

    public List<ServiceReview> findByMemberId(Long memberId) {
        return reviewRepository.findByMemberId(memberId);
    }

    public List<ServiceReview> findByStoreId(Long storeId) {
        return reviewRepository.findByStoreIdOrderByCreatedAtDesc(storeId);
    }

    public List<ServiceReview> findByEmployeeId(Long employeeId) {
        return reviewRepository.findByEmployeeId(employeeId);
    }

    @Transactional
    public ServiceReview createReview(ServiceReview review) {
        return reviewRepository.save(review);
    }

    @Transactional
    public ServiceReview submitReview(Long orderId, Long memberId, Long storeId, 
                                      Long employeeId, Integer rating, String content, 
                                      String images, Boolean anonymous) {
        if (reviewRepository.findByOrderIdAndMemberId(orderId, memberId).isPresent()) {
            return null;
        }
        
        ServiceReview review = new ServiceReview();
        review.setOrderId(orderId);
        review.setMemberId(memberId);
        review.setStoreId(storeId);
        review.setEmployeeId(employeeId);
        review.setRating(rating);
        review.setContent(content);
        review.setImages(images);
        review.setAnonymous(anonymous != null ? anonymous : false);
        review.setStatus(1);
        return reviewRepository.save(review);
    }

    @Transactional
    public boolean replyReview(Long reviewId, String reply) {
        return reviewRepository.findById(reviewId).map(review -> {
            review.setReply(reply);
            review.setReplyTime(LocalDateTime.now());
            reviewRepository.save(review);
            return true;
        }).orElse(false);
    }

    public Double getAverageRatingByStore(Long storeId) {
        List<ServiceReview> reviews = reviewRepository.findByStoreId(storeId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(ServiceReview::getRating)
                .average()
                .orElse(0.0);
    }

    public Double getAverageRatingByEmployee(Long employeeId) {
        List<ServiceReview> reviews = reviewRepository.findByEmployeeId(employeeId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(ServiceReview::getRating)
                .average()
                .orElse(0.0);
    }
}
