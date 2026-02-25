package org.example.store.repository;

import org.example.store.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    
    List<Employee> findByStoreId(Long storeId);
    
    Optional<Employee> findByPhone(String phone);
    
    List<Employee> findByStoreIdAndStatus(Long storeId, Integer status);
}
