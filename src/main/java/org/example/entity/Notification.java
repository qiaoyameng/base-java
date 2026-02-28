package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @Column(length = 100)
    private String title;

    @Column(length = 500)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NotificationType type;

    private Long relatedId;

    private Boolean isRead = false;

    private LocalDateTime readTime;

    @CreationTimestamp
    private LocalDateTime createTime;

    public enum NotificationType {
        ORDER("订单通知"),
        COUPON("优惠券通知"),
        ACTIVITY("活动通知"),
        SYSTEM("系统通知");

        private String desc;

        NotificationType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
