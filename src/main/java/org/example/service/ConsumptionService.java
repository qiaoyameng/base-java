package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dto.ReviewDTO;
import org.example.entity.*;
import org.example.exception.BusinessException;
import org.example.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsumptionService {
    private final ConsumptionRecordRepository consumptionRecordRepository;
    private final ProductReviewRepository productReviewRepository;
    private final ElectronicReceiptRepository electronicReceiptRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ConsumptionRecord createConsumptionRecord(Order order) {
        ConsumptionRecord record = new ConsumptionRecord();
        record.setMemberId(order.getMemberId());
        record.setOrderId(order.getId());
        record.setOrderNo(order.getOrderNo());
        record.setAmount(order.getPayAmount());
        record.setPointsEarned(order.getPayAmount().intValue());
        record.setType("PURCHASE");
        record.setRemark("购物消费");
        return consumptionRecordRepository.save(record);
    }

    public Page<ConsumptionRecord> getMemberConsumptions(Long memberId, Pageable pageable) {
        return consumptionRecordRepository.findByMemberIdOrderByCreateTimeDesc(memberId, pageable);
    }

    public BigDecimal getTotalConsumption(Long memberId) {
        BigDecimal total = consumptionRecordRepository.sumAmountByMemberId(memberId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Integer getTotalPointsEarned(Long memberId) {
        Integer total = consumptionRecordRepository.sumPointsByMemberId(memberId);
        return total != null ? total : 0;
    }

    @Transactional
    public ProductReview createReview(Long memberId, ReviewDTO dto) {
        Product product = productRepository.findByIdAndDeletedFalse(dto.getProductId())
                .orElseThrow(() -> new BusinessException("商品不存在"));

        if (dto.getOrderId() != null) {
            Order order = orderRepository.findByIdAndDeletedFalse(dto.getOrderId())
                    .orElseThrow(() -> new BusinessException("订单不存在"));

            if (!order.getMemberId().equals(memberId)) {
                throw new BusinessException("无权评价该订单");
            }

            if (productReviewRepository.existsByMemberIdAndOrderIdAndProductIdAndDeletedFalse(
                    memberId, dto.getOrderId(), dto.getProductId())) {
                throw new BusinessException("该商品已评价");
            }
        }

        ProductReview review = new ProductReview();
        review.setMemberId(memberId);
        review.setProductId(dto.getProductId());
        review.setOrderId(dto.getOrderId());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            review.setImages(String.join(",", dto.getImages()));
        }
        review.setAnonymous(dto.getAnonymous());
        return productReviewRepository.save(review);
    }

    public Page<ProductReview> getProductReviews(Long productId, Pageable pageable) {
        return productReviewRepository.findByProductIdAndDeletedFalse(productId, pageable);
    }

    public Page<ProductReview> getMemberReviews(Long memberId, Pageable pageable) {
        return productReviewRepository.findByMemberIdAndDeletedFalse(memberId, pageable);
    }

    public Double getAverageRating(Long productId) {
        return productReviewRepository.getAverageRating(productId);
    }

    public Long getReviewCount(Long productId) {
        return productReviewRepository.countByProductId(productId);
    }

    @Transactional
    public void deleteReview(Long id, Long memberId) {
        ProductReview review = productReviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException("评价不存在"));

        if (!review.getMemberId().equals(memberId)) {
            throw new BusinessException("无权删除该评价");
        }

        review.setDeleted(true);
        productReviewRepository.save(review);
    }

    @Transactional
    public ElectronicReceipt generateReceipt(Long orderId) {
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (electronicReceiptRepository.findByOrderId(orderId).isPresent()) {
            return electronicReceiptRepository.findByOrderId(orderId).get();
        }

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        String itemsJson = items.stream()
                .map(item -> String.format("{\"name\":\"%s\",\"price\":%s,\"qty\":%d}",
                        item.getProductName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.joining(",", "[", "]"));

        ElectronicReceipt receipt = new ElectronicReceipt();
        receipt.setReceiptNo(generateReceiptNo());
        receipt.setMemberId(order.getMemberId());
        receipt.setOrderId(orderId);
        receipt.setOrderNo(order.getOrderNo());
        receipt.setTotalAmount(order.getTotalAmount());
        receipt.setPayAmount(order.getPayAmount());
        receipt.setItems(itemsJson);
        receipt.setQrCode(generateQrCode(receipt.getReceiptNo()));
        return electronicReceiptRepository.save(receipt);
    }

    private String generateReceiptNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "RCP" + timestamp + random;
    }

    private String generateQrCode(String receiptNo) {
        return "QR_" + receiptNo + "_" + System.currentTimeMillis();
    }

    public ElectronicReceipt getReceiptByOrderId(Long orderId) {
        return electronicReceiptRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException("电子凭证不存在"));
    }

    public ElectronicReceipt getReceiptByReceiptNo(String receiptNo) {
        return electronicReceiptRepository.findByReceiptNo(receiptNo)
                .orElseThrow(() -> new BusinessException("电子凭证不存在"));
    }

    public ElectronicReceipt getMemberReceipt(Long memberId, Long orderId) {
        return electronicReceiptRepository.findByMemberIdAndOrderId(memberId, orderId)
                .orElseThrow(() -> new BusinessException("电子凭证不存在"));
    }
}
