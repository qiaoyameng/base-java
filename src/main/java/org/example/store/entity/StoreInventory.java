package org.example.store.entity;

import jakarta.persistence.*;
import org.example.common.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "store_inventory")
public class StoreInventory extends BaseEntity {
    
    @Column(name = "store_id", nullable = false)
    private Long storeId;
    
    @Column(name = "material_name", nullable = false, length = 100)
    private String materialName;
    
    @Column(name = "material_type", length = 50)
    private String materialType;
    
    @Column(name = "unit", length = 20)
    private String unit;
    
    @Column(name = "quantity")
    private Integer quantity = 0;
    
    @Column(name = "min_quantity")
    private Integer minQuantity = 10;
    
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    
    @Column(name = "supplier", length = 100)
    private String supplier;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
