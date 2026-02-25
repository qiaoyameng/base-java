package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ReviewRequest;
import org.example.dto.ReviewResponse;
import org.example.entity.Review;
import org.example.entity.WashOrder;
import org.example.enums.OrderStatus;
import org.example.exception.BusinessException;
import org.example.mapstruct.ReviewMapper;
import org.example.repository.ReviewRepository;
import org.example.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        WashOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new BusinessException("只能对已完成订单进行评价");
        }

        if (!reviewRepository.findByOrderId(request.getOrderId()).isEmpty()) {
            throw new BusinessException("该订单已评价");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setCustomer(order.getCustomer());
        review.setService(order.getService());
        review.setStore(order.getStore());
        review.setRating(request.getRating());
        review.setContent(request.getComment());
        review.setVisible(true);

        Review saved = reviewRepository.save(review);
        log.info("评价创建成功: orderNo={}, rating={}", order.getOrderNo(), request.getRating());
        return reviewMapper.toResponse(saved);
    }

    @Transactional
    public ReviewResponse replyReview(Long reviewId, String reply) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException("评价不存在"));
        review.setReply(reply);
        Review saved = reviewRepository.save(review);
        log.info("评价回复成功: reviewId={}", reviewId);
        return reviewMapper.toResponse(saved);
    }

    public List<ReviewResponse> getReviewsByStore(Long storeId, Integer rating) {
        List<Review> reviews;
        if (rating != null) {
            reviews = reviewRepository.findByStoreId(storeId).stream()
                    .filter(r -> r.getRating().equals(rating) && Boolean.TRUE.equals(r.getVisible()))
                    .toList();
        } else {
            reviews = reviewRepository.findByStoreIdAndVisible(storeId, true);
        }
        return reviews.stream().map(reviewMapper::toResponse).toList();
    }

    public List<ReviewResponse> getReviewsByCustomer(Long customerId) {
        List<Review> reviews = reviewRepository.findByCustomerId(customerId).stream()
                .filter(r -> Boolean.TRUE.equals(r.getVisible()))
                .toList();
        return reviews.stream().map(reviewMapper::toResponse).toList();
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException("评价不存在"));
        review.setVisible(false);
        reviewRepository.save(review);
        log.info("评价删除成功: reviewId={}", reviewId);
    }

    public ReviewResponse getReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException("评价不存在"));
        return reviewMapper.toResponse(review);
    }
}
