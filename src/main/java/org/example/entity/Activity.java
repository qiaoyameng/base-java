package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 500)
    private String summary;

    @Column(length = 500)
    private String coverImage;

    @Column(length = 2000)
    private String images;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ActivityType type;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isTop = false;

    private Integer views = 0;

    private Boolean enabled = true;

    @CreationTimestamp
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    public enum ActivityType {
        PROMOTION("促销活动"),
        ANNOUNCEMENT("公告通知"),
        NEW_ARRIVAL("新品上市"),
        EVENT("线下活动");

        private String desc;

        ActivityType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
