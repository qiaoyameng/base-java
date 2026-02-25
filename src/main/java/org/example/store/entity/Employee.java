package org.example.store.entity;

import jakarta.persistence.*;
import org.example.common.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
public class Employee extends BaseEntity {
    
    @Column(name = "store_id", nullable = false)
    private Long storeId;
    
    @Column(name = "employee_name", nullable = false, length = 50)
    private String employeeName;
    
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    
    @Column(name = "password", length = 100)
    private String password;
    
    @Column(name = "role", length = 20)
    private String role;
    
    @Column(name = "status")
    private Integer status = 1;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @Column(name = "id_card", length = 20)
    private String idCard;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
