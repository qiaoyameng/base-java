package org.example.service;

import org.example.dto.ConsumptionRecordDTO;
import org.example.dto.ReceiptDTO;
import org.example.dto.ReviewDTO;
import org.example.common.PageResult;

public interface ConsumptionService {

    ConsumptionRecordDTO createConsumptionRecord(Long orderId);

    PageResult<ConsumptionRecordDTO> getCustomerConsumptionRecords(Long customerId, Integer pageNum, Integer pageSize);

    Double getTotalConsumption(Long customerId);

    ReviewDTO createReview(ReviewDTO reviewDTO);

    ReviewDTO replyReview(Long reviewId, String reply);

    void likeReview(Long reviewId);

    PageResult<ReviewDTO> getProductReviews(Long productId, Integer pageNum, Integer pageSize);

    PageResult<ReviewDTO> getCustomerReviews(Long customerId, Integer pageNum, Integer pageSize);

    ReceiptDTO generateReceipt(Long orderId);

    ReceiptDTO getReceiptByOrderId(Long orderId);

    ReceiptDTO getReceiptByNo(String receiptNo);

    ReceiptDTO verifyReceipt(String verificationCode);
}
