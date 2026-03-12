package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "member_points_log")
public class MemberPointsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private Integer pointsAfter;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(length = 200)
    private String remark;

    @Column
    private Long relatedOrderId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTime;
}
