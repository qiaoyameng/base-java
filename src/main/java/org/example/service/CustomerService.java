package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CustomerResponse;
import org.example.entity.Customer;
import org.example.enums.MemberLevel;
import org.example.exception.BusinessException;
import org.example.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("客户不存在"));
    }

    public Customer getCustomerByUserId(Long userId) {
        return customerRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("客户不存在"));
    }

    public MemberLevel calculateMemberLevel(BigDecimal totalPoints) {
        if (totalPoints.compareTo(BigDecimal.valueOf(5000)) >= 0) {
            return MemberLevel.GOLD;
        } else if (totalPoints.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            return MemberLevel.SILVER;
        }
        return MemberLevel.NORMAL;
    }

    @Transactional
    public void updateMemberLevel(Long customerId) {
        Customer customer = getCustomerById(customerId);
        MemberLevel newLevel = calculateMemberLevel(customer.getTotalPoints());
        if (customer.getMemberLevel() != newLevel) {
            customer.setMemberLevel(newLevel);
            customerRepository.save(customer);
            log.info("客户会员等级变更: customerId={}, oldLevel={}, newLevel={}",
                    customerId, customer.getMemberLevel(), newLevel);
        }
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer request) {
        Customer customer = getCustomerById(id);
        customer.setNickname(request.getNickname());
        customer.setPhone(request.getPhone());
        customer.setAvatar(request.getAvatar());
        customer.setGender(request.getGender());
        customer.setBirthday(request.getBirthday());
        customer.setAddress(request.getAddress());
        return customerRepository.save(customer);
    }

    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse resp = new CustomerResponse();
        resp.setId(customer.getId());
        resp.setUserId(customer.getUser() != null ? customer.getUser().getId() : null);
        resp.setNickname(customer.getNickname());
        resp.setPhone(customer.getPhone());
        resp.setEmail(customer.getEmail());
        resp.setMemberLevel(customer.getMemberLevel());
        resp.setTotalConsumption(customer.getTotalConsumption());
        resp.setTotalPoints(customer.getTotalPoints());
        resp.setAvailablePoints(customer.getAvailablePoints());
        resp.setOrderCount(customer.getOrderCount());
        resp.setEnabled(customer.getEnabled());
        return resp;
    }
}
