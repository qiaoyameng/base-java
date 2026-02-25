package org.example.order.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.common.BaseEntity;
import org.example.common.enums.ServiceCategory;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_item")
public class OrderItem extends BaseEntity {
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "service_id", nullable = false)
    private Long serviceId;
    
    @Column(name = "service_name", length = 100)
    private String serviceName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ServiceCategory category;
    
    @Column(name = "quantity")
    private Integer quantity = 1;
    
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "clothes_name", length = 100)
    private String clothesName;
    
    @Column(name = "clothes_color", length = 50)
    private String clothesColor;
    
    @Column(name = "clothes_brand", length = 50)
    private String clothesBrand;
    
    @Column(name = "clothes_image", length = 255)
    private String clothesImage;
    
    @Column(name = "special_note", length = 255)
    private String specialNote;
    
    @Column(name = "item_status")
    private String itemStatus;
}
