package org.example.record.entity;

import jakarta.persistence.*;
import org.example.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_record")
public class ServiceRecord extends BaseEntity {
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "order_item_id")
    private Long orderItemId;
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    @Column(name = "store_id")
    private Long storeId;
    
    @Column(name = "employee_id")
    private Long employeeId;
    
    @Column(name = "service_id")
    private Long serviceId;
    
    @Column(name = "service_name", length = 100)
    private String serviceName;
    
    @Column(name = "clothes_name", length = 100)
    private String clothesName;
    
    @Column(name = "clothes_image", length = 255)
    private String clothesImage;
    
    @Column(name = "receive_time")
    private LocalDateTime receiveTime;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;
    
    @Column(name = "actual_duration")
    private Integer actualDuration;
    
    @Column(name = "status", length = 20)
    private String status;
    
    @Column(name = "before_images", length = 500)
    private String beforeImages;
    
    @Column(name = "after_images", length = 500)
    private String afterImages;
    
    @Column(name = "remark", length = 500)
    private String remark;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getClothesName() { return clothesName; }
    public void setClothesName(String clothesName) { this.clothesName = clothesName; }
    public String getClothesImage() { return clothesImage; }
    public void setClothesImage(String clothesImage) { this.clothesImage = clothesImage; }
    public LocalDateTime getReceiveTime() { return receiveTime; }
    public void setReceiveTime(LocalDateTime receiveTime) { this.receiveTime = receiveTime; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public LocalDateTime getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(LocalDateTime deliveryTime) { this.deliveryTime = deliveryTime; }
    public Integer getActualDuration() { return actualDuration; }
    public void setActualDuration(Integer actualDuration) { this.actualDuration = actualDuration; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBeforeImages() { return beforeImages; }
    public void setBeforeImages(String beforeImages) { this.beforeImages = beforeImages; }
    public String getAfterImages() { return afterImages; }
    public void setAfterImages(String afterImages) { this.afterImages = afterImages; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
