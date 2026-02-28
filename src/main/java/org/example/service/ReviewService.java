package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Customer;
import org.example.entity.OrderItem;
import org.example.entity.ProductReview;
import org.example.repository.CustomerRepository;
import org.example.repository.OrderItemRepository;
import org.example.repository.ProductReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public ProductReview createReview(Long customerId, Long orderItemId, Integer rating, String content, String images, Boolean isAnonymous) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("订单项不存在"));

        if (orderItem.getReviewed()) {
            throw new RuntimeException("该商品已经评价过");
        }

        Customer customer = customerRepository.findById(customerId).orElse(null);

        ProductReview review = new ProductReview();
        review.setCustomerId(customerId);
        if (customer != null) {
            review.setCustomerNickname(isAnonymous ? "匿名用户" : customer.getNickname());
            review.setCustomerAvatar(isAnonymous ? null : customer.getAvatar());
        }
        review.setProductId(orderItem.getProductId());
        review.setOrderId(orderItem.getOrderId());
        review.setOrderItemId(orderItemId);
        review.setRating(rating);
        review.setContent(content);
        review.setImages(images);
        review.setIsAnonymous(isAnonymous);
        review.setEnabled(true);

        orderItem.setReviewed(true);
        orderItemRepository.save(orderItem);

        return productReviewRepository.save(review);
    }

    public Page<ProductReview> findByProductId(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "isTop", "createTime"));
        return productReviewRepository.findByProductIdAndEnabled(productId, true, pageable);
    }

    public List<ProductReview> findByProductId(Long productId) {
        return productReviewRepository.findByProductIdAndEnabled(productId, true);
    }

    public Page<ProductReview> findByCustomerId(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return productReviewRepository.findByCustomerId(customerId, pageable);
    }

    public List<ProductReview> findByOrderId(Long orderId) {
        return productReviewRepository.findByOrderId(orderId);
    }

    public ProductReview findById(Long id) {
        return productReviewRepository.findById(id).orElse(null);
    }

    @Transactional
    public ProductReview reply(Long id, String reply) {
        ProductReview review = findById(id);
        if (review != null) {
            review.setReply(reply);
            review.setReplyTime(java.time.LocalDateTime.now());
            return productReviewRepository.save(review);
        }
        return null;
    }

    @Transactional
    public void toggleTop(Long id, Boolean isTop) {
        ProductReview review = findById(id);
        if (review != null) {
            review.setIsTop(isTop);
            productReviewRepository.save(review);
        }
    }

    @Transactional
    public void toggleEnabled(Long id, Boolean enabled) {
        ProductReview review = findById(id);
        if (review != null) {
            review.setEnabled(enabled);
            productReviewRepository.save(review);
        }
    }

    @Transactional
    public void delete(Long id) {
        productReviewRepository.deleteById(id);
    }
}
