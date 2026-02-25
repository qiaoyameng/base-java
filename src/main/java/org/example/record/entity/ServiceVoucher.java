package org.example.record.entity;

import jakarta.persistence.*;
import org.example.common.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_voucher")
public class ServiceVoucher extends BaseEntity {
    
    @Column(name = "voucher_no", unique = true, length = 50)
    private String voucherNo;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    @Column(name = "store_id")
    private Long storeId;
    
    @Column(name = "voucher_type", length = 20)
    private String voucherType;
    
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "pay_amount", precision = 10, scale = 2)
    private BigDecimal payAmount;
    
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;
    
    @Column(name = "service_details", columnDefinition = "TEXT")
    private String serviceDetails;
    
    @Column(name = "generate_time")
    private LocalDateTime generateTime;
    
    @Column(name = "status")
    private Integer status = 1;
    
    @Column(name = "pdf_url", length = 255)
    private String pdfUrl;

    public String getVoucherNo() { return voucherNo; }
    public void setVoucherNo(String voucherNo) { this.voucherNo = voucherNo; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public String getVoucherType() { return voucherType; }
    public void setVoucherType(String voucherType) { this.voucherType = voucherType; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getPayAmount() { return payAmount; }
    public void setPayAmount(BigDecimal payAmount) { this.payAmount = payAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getServiceDetails() { return serviceDetails; }
    public void setServiceDetails(String serviceDetails) { this.serviceDetails = serviceDetails; }
    public LocalDateTime getGenerateTime() { return generateTime; }
    public void setGenerateTime(LocalDateTime generateTime) { this.generateTime = generateTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getPdfUrl() { return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }
}
