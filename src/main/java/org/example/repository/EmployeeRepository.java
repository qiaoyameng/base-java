package org.example.repository;

import org.example.entity.Employee;
import org.example.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUserId(Long userId);
    List<Employee> findByStoreId(Long storeId);
    List<Employee> findByStoreIdAndRole(Long storeId, RoleType role);
    Optional<Employee> findByEmployeeNo(String employeeNo);
}
