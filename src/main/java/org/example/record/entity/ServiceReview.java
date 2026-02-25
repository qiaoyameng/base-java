package org.example.record.entity;

import jakarta.persistence.*;
import org.example.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_review")
public class ServiceReview extends BaseEntity {
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "service_record_id")
    private Long serviceRecordId;
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    @Column(name = "store_id")
    private Long storeId;
    
    @Column(name = "employee_id")
    private Long employeeId;
    
    @Column(name = "rating")
    private Integer rating;
    
    @Column(name = "content", length = 500)
    private String content;
    
    @Column(name = "images", length = 500)
    private String images;
    
    @Column(name = "reply", length = 255)
    private String reply;
    
    @Column(name = "reply_time")
    private LocalDateTime replyTime;
    
    @Column(name = "anonymous")
    private Boolean anonymous = false;
    
    @Column(name = "status")
    private Integer status = 1;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getServiceRecordId() { return serviceRecordId; }
    public void setServiceRecordId(Long serviceRecordId) { this.serviceRecordId = serviceRecordId; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }
    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
    public LocalDateTime getReplyTime() { return replyTime; }
    public void setReplyTime(LocalDateTime replyTime) { this.replyTime = replyTime; }
    public Boolean getAnonymous() { return anonymous; }
    public void setAnonymous(Boolean anonymous) { this.anonymous = anonymous; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
