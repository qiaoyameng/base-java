package org.example.store.service;

import org.example.store.entity.Employee;
import org.example.store.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findByStoreId(Long storeId) {
        return employeeRepository.findByStoreId(storeId);
    }

    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> findByPhone(String phone) {
        return employeeRepository.findByPhone(phone);
    }

    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee update(Long id, Employee employee) {
        return employeeRepository.findById(id).map(existing -> {
            existing.setEmployeeName(employee.getEmployeeName());
            existing.setPhone(employee.getPhone());
            existing.setRole(employee.getRole());
            existing.setStatus(employee.getStatus());
            existing.setIdCard(employee.getIdCard());
            return employeeRepository.save(existing);
        }).orElse(null);
    }

    @Transactional
    public boolean updatePassword(Long id, String password) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setPassword(password);
            employeeRepository.save(employee);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean delete(Long id) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setDeleted(true);
            employeeRepository.save(employee);
            return true;
        }).orElse(false);
    }

    public List<Employee> findActiveByStoreId(Long storeId) {
        return employeeRepository.findByStoreIdAndStatus(storeId, 1);
    }
}
