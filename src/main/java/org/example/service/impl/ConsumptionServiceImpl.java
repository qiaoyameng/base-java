package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.ConsumptionRecordDTO;
import org.example.dto.ReceiptDTO;
import org.example.dto.ReviewDTO;
import org.example.entity.*;
import org.example.common.PageResult;
import org.example.repository.*;
import org.example.service.ConsumptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsumptionServiceImpl implements ConsumptionService {

    private final ConsumptionRecordRepository consumptionRecordRepository;
    private final ReviewRepository reviewRepository;
    private final ReceiptRepository receiptRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ConsumptionRecordDTO createConsumptionRecord(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        ConsumptionRecord record = new ConsumptionRecord();
        record.setCustomerId(order.getCustomerId());
        record.setCustomerName(order.getCustomerName());
        record.setOrderId(order.getId());
        record.setOrderNo(order.getOrderNo());
        record.setConsumptionAmount(order.getPayAmount());
        record.setEarnedPoints(order.getPayAmount().intValue());
        record.setUsedPoints(order.getUsedPoints() != null ? order.getUsedPoints().intValue() : 0);
        record.setPointsDiscount(order.getPointsDiscount());
        record.setConsumptionTime(LocalDateTime.now());
        record.setDeleted(false);

        ConsumptionRecord savedRecord = consumptionRecordRepository.save(record);
        return convertToConsumptionRecordDTO(savedRecord);
    }

    @Override
    public PageResult<ConsumptionRecordDTO> getCustomerConsumptionRecords(Long customerId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ConsumptionRecord> recordPage = consumptionRecordRepository.findByCustomerIdAndDeletedFalse(customerId, pageable);

        List<ConsumptionRecordDTO> dtoList = recordPage.getContent().stream()
                .map(this::convertToConsumptionRecordDTO)
                .collect(Collectors.toList());

        Page<ConsumptionRecordDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(
                dtoList, pageable, recordPage.getTotalElements());

        return PageResult.of(dtoPage);
    }

    @Override
    public Double getTotalConsumption(Long customerId) {
        Double total = consumptionRecordRepository.sumConsumptionByCustomerId(customerId);
        return total != null ? total : 0.0;
    }

    @Override
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Order order = orderRepository.findById(reviewDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getStatus().ordinal() < org.example.enums.OrderStatus.COMPLETED.ordinal()) {
            throw new RuntimeException("订单未完成，无法评价");
        }

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        Review review = new Review();
        review.setCustomerId(order.getCustomerId());
        review.setCustomerName(order.getCustomerName());
        review.setProductId(product.getId());
        review.setProductName(product.getName());
        review.setOrderId(order.getId());
        review.setRating(reviewDTO.getRating());
        review.setContent(reviewDTO.getContent());
        review.setImages(reviewDTO.getImages());
        review.setLikeCount(0);
        review.setVerified(true);
        review.setDeleted(false);

        Review savedReview = reviewRepository.save(review);

        updateProductRating(product.getId());

        return convertToReviewDTO(savedReview);
    }

    @Override
    @Transactional
    public ReviewDTO replyReview(Long reviewId, String reply) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("评价不存在"));
        review.setReply(reply);
        Review savedReview = reviewRepository.save(review);
        return convertToReviewDTO(savedReview);
    }

    @Override
    @Transactional
    public void likeReview(Long reviewId) {
        reviewRepository.incrementLikeCount(reviewId);
    }

    @Override
    public PageResult<ReviewDTO> getProductReviews(Long productId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Review> reviewPage = reviewRepository.findByProductIdAndDeletedFalse(productId, pageable);

        List<ReviewDTO> dtoList = reviewPage.getContent().stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());

        Page<ReviewDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(
                dtoList, pageable, reviewPage.getTotalElements());

        return PageResult.of(dtoPage);
    }

    @Override
    public PageResult<ReviewDTO> getCustomerReviews(Long customerId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Review> reviewPage = reviewRepository.findByCustomerIdAndDeletedFalse(customerId, pageable);

        List<ReviewDTO> dtoList = reviewPage.getContent().stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());

        Page<ReviewDTO> dtoPage = new org.springframework.data.domain.PageImpl<>(
                dtoList, pageable, reviewPage.getTotalElements());

        return PageResult.of(dtoPage);
    }

    @Override
    @Transactional
    public ReceiptDTO generateReceipt(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        Receipt receipt = new Receipt();
        receipt.setReceiptNo(generateReceiptNo());
        receipt.setOrderId(order.getId());
        receipt.setOrderNo(order.getOrderNo());
        receipt.setCustomerId(order.getCustomerId());
        receipt.setCustomerName(order.getCustomerName());
        receipt.setTotalAmount(order.getTotalAmount());
        receipt.setDiscountAmount(order.getDiscountAmount());
        receipt.setPayAmount(order.getPayAmount());
        receipt.setIssueTime(LocalDateTime.now());
        receipt.setQrCode(generateQRCode(order.getOrderNo()));
        receipt.setVerificationCode(generateVerificationCode());
        receipt.setVerified(false);

        Receipt savedReceipt = receiptRepository.save(receipt);
        return convertToReceiptDTO(savedReceipt);
    }

    @Override
    public ReceiptDTO getReceiptByOrderId(Long orderId) {
        Receipt receipt = receiptRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("凭证不存在"));
        return convertToReceiptDTO(receipt);
    }

    @Override
    public ReceiptDTO getReceiptByNo(String receiptNo) {
        Receipt receipt = receiptRepository.findByReceiptNo(receiptNo)
                .orElseThrow(() -> new RuntimeException("凭证不存在"));
        return convertToReceiptDTO(receipt);
    }

    @Override
    @Transactional
    public ReceiptDTO verifyReceipt(String verificationCode) {
        Receipt receipt = receiptRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new RuntimeException("凭证不存在"));

        if (receipt.getVerified()) {
            throw new RuntimeException("凭证已验证");
        }

        receipt.setVerified(true);
        receipt.setVerifiedTime(LocalDateTime.now());
        Receipt savedReceipt = receiptRepository.save(receipt);
        return convertToReceiptDTO(savedReceipt);
    }

    private void updateProductRating(Long productId) {
        Double avgRating = reviewRepository.calculateAverageRating(productId);
        Long count = reviewRepository.countByProductId(productId);

        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setRating(avgRating != null ? avgRating : 5.0);
            productRepository.save(product);
        }
    }

    private String generateReceiptNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return "RCP" + timestamp + random;
    }

    private String generateQRCode(String orderNo) {
        return "QR" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
    }

    private String generateVerificationCode() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }

    private ConsumptionRecordDTO convertToConsumptionRecordDTO(ConsumptionRecord record) {
        ConsumptionRecordDTO dto = new ConsumptionRecordDTO();
        BeanUtils.copyProperties(record, dto);
        return dto;
    }

    private ReviewDTO convertToReviewDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        BeanUtils.copyProperties(review, dto);
        return dto;
    }

    private ReceiptDTO convertToReceiptDTO(Receipt receipt) {
        ReceiptDTO dto = new ReceiptDTO();
        BeanUtils.copyProperties(receipt, dto);
        return dto;
    }
}
