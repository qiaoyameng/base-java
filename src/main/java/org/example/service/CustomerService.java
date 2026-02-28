package org.example.service;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.example.entity.Customer;
import org.example.entity.StoredValueLog;
import org.example.repository.CustomerRepository;
import org.example.repository.StoredValueLogRepository;
import org.example.util.RedisUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final StoredValueLogRepository storedValueLogRepository;
    private final RedisUtil redisUtil;

    private static final String CUSTOMER_CACHE_KEY = "customer:";

    public Customer register(Customer customer) {
        if (customerRepository.findByPhone(customer.getPhone()).isPresent()) {
            throw new RuntimeException("手机号已注册");
        }
        customer.setMemberLevel(Customer.MemberLevel.NORMAL);
        customer.setPoints(0);
        customer.setTotalConsumed(BigDecimal.ZERO);
        customer.setStoredValue(BigDecimal.ZERO);
        customer.setCardNo(generateCardNo());
        customer.setEnabled(true);
        return customerRepository.save(customer);
    }

    private String generateCardNo() {
        return "VIP" + System.currentTimeMillis() + IdUtil.randomNumbers(4);
    }

    public Customer findById(Long id) {
        String cacheKey = CUSTOMER_CACHE_KEY + id;
        Customer cached = (Customer) redisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            redisUtil.setEx(cacheKey, customer, 3600L);
        }
        return customer;
    }

    public Customer findByPhone(String phone) {
        return customerRepository.findByPhone(phone).orElse(null);
    }

    public Page<Customer> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return customerRepository.findAll(pageable);
    }

    public Page<Customer> findByLevel(Customer.MemberLevel level, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return customerRepository.findByMemberLevel(level, pageable);
    }

    public Customer update(Long id, Customer update) {
        Customer customer = findById(id);
        if (customer != null) {
            if (update.getNickname() != null) customer.setNickname(update.getNickname());
            if (update.getAvatar() != null) customer.setAvatar(update.getAvatar());
            if (update.getGender() != null) customer.setGender(update.getGender());
            if (update.getBirthday() != null) customer.setBirthday(update.getBirthday());
            if (update.getEmail() != null) customer.setEmail(update.getEmail());
            if (update.getAddress() != null) customer.setAddress(update.getAddress());
            clearCache(id);
            return customerRepository.save(customer);
        }
        return null;
    }

    @Transactional
    public void addPoints(Long id, int pointsToAdd) {
        Customer customer = findById(id);
        if (customer != null && pointsToAdd > 0) {
            customer.setPoints(customer.getPoints() + pointsToAdd);
            checkAndUpgradeLevel(customer);
            customerRepository.save(customer);
            clearCache(id);
        }
    }

    @Transactional
    public int deductPoints(Long id, int pointsToDeduct, BigDecimal orderAmount) {
        Customer customer = findById(id);
        if (customer == null) {
            throw new RuntimeException("会员不存在");
        }
        if (pointsToDeduct <= 0) {
            throw new RuntimeException("抵扣积分必须大于0");
        }
        if (customer.getPoints() < pointsToDeduct) {
            throw new RuntimeException("积分不足");
        }
        
        int maxDeduct = orderAmount.multiply(BigDecimal.valueOf(0.5)).intValue();
        int actualDeduct = Math.min(pointsToDeduct, maxDeduct);
        
        customer.setPoints(customer.getPoints() - actualDeduct);
        customerRepository.save(customer);
        clearCache(id);
        
        return actualDeduct;
    }

    private void checkAndUpgradeLevel(Customer customer) {
        BigDecimal total = customer.getTotalConsumed();
        Customer.MemberLevel current = customer.getMemberLevel();
        Customer.MemberLevel newLevel = current;

        if (total.compareTo(BigDecimal.valueOf(10000)) >= 0 && current.ordinal() < Customer.MemberLevel.GOLD.ordinal()) {
            newLevel = Customer.MemberLevel.GOLD;
        } else if (total.compareTo(BigDecimal.valueOf(2000)) >= 0 && current.ordinal() < Customer.MemberLevel.SILVER.ordinal()) {
            newLevel = Customer.MemberLevel.SILVER;
        }

        if (newLevel != current) {
            customer.setMemberLevel(newLevel);
        }
    }

    @Transactional
    public StoredValueLog recharge(Long id, BigDecimal amount, String description) {
        Customer customer = findById(id);
        if (customer == null) {
            throw new RuntimeException("会员不存在");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("充值金额必须大于0");
        }

        BigDecimal before = customer.getStoredValue();
        BigDecimal after = before.add(amount);

        customer.setStoredValue(after);
        customerRepository.save(customer);
        clearCache(id);

        StoredValueLog log = new StoredValueLog();
        log.setCustomerId(id);
        log.setCardNo(customer.getCardNo());
        log.setType(StoredValueLog.LogType.RECHARGE);
        log.setAmount(amount);
        log.setBalanceBefore(before);
        log.setBalanceAfter(after);
        log.setDescription(description != null ? description : "储值卡充值");

        return storedValueLogRepository.save(log);
    }

    @Transactional
    public BigDecimal useStoredValue(Long id, BigDecimal amount, Long orderId, String orderNo) {
        Customer customer = findById(id);
        if (customer == null) {
            throw new RuntimeException("会员不存在");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("消费金额必须大于0");
        }

        BigDecimal before = customer.getStoredValue();
        if (before.compareTo(amount) < 0) {
            throw new RuntimeException("储值卡余额不足");
        }

        BigDecimal after = before.subtract(amount);

        customer.setStoredValue(after);
        customerRepository.save(customer);
        clearCache(id);

        StoredValueLog log = new StoredValueLog();
        log.setCustomerId(id);
        log.setCardNo(customer.getCardNo());
        log.setType(StoredValueLog.LogType.CONSUME);
        log.setAmount(amount);
        log.setBalanceBefore(before);
        log.setBalanceAfter(after);
        log.setDescription("订单消费");
        log.setRelatedOrderId(orderId);
        log.setRelatedOrderNo(orderNo);

        storedValueLogRepository.save(log);

        return after;
    }

    @Transactional
    public void refundStoredValue(Long id, BigDecimal amount, Long orderId, String orderNo) {
        Customer customer = findById(id);
        if (customer == null) {
            throw new RuntimeException("会员不存在");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal before = customer.getStoredValue();
        BigDecimal after = before.add(amount);

        customer.setStoredValue(after);
        customerRepository.save(customer);
        clearCache(id);

        StoredValueLog log = new StoredValueLog();
        log.setCustomerId(id);
        log.setCardNo(customer.getCardNo());
        log.setType(StoredValueLog.LogType.REFUND);
        log.setAmount(amount);
        log.setBalanceBefore(before);
        log.setBalanceAfter(after);
        log.setDescription("订单退款");
        log.setRelatedOrderId(orderId);
        log.setRelatedOrderNo(orderNo);

        storedValueLogRepository.save(log);
    }

    @Transactional
    public void addTotalConsumed(Long id, BigDecimal amount) {
        Customer customer = findById(id);
        if (customer != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            customer.setTotalConsumed(customer.getTotalConsumed().add(amount));
            checkAndUpgradeLevel(customer);
            customerRepository.save(customer);
            clearCache(id);
        }
    }

    public Page<StoredValueLog> findStoredValueLogs(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return storedValueLogRepository.findByCustomerId(customerId, pageable);
    }

    public Page<StoredValueLog> findStoredValueLogsByType(Long customerId, StoredValueLog.LogType type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return storedValueLogRepository.findByCustomerIdAndType(customerId, type, pageable);
    }

    @Transactional
    public void toggleEnabled(Long id, Boolean enabled) {
        Customer customer = findById(id);
        if (customer != null) {
            customer.setEnabled(enabled);
            customerRepository.save(customer);
            clearCache(id);
        }
    }

    @Transactional
    public Customer.MemberLevel upgradeLevel(Long id, Customer.MemberLevel newLevel) {
        Customer customer = findById(id);
        if (customer != null) {
            if (newLevel.ordinal() <= customer.getMemberLevel().ordinal()) {
                throw new RuntimeException("只能升级到更高等级");
            }
            customer.setMemberLevel(newLevel);
            customerRepository.save(customer);
            clearCache(id);
            return newLevel;
        }
        return null;
    }

    private void clearCache(Long id) {
        redisUtil.del(CUSTOMER_CACHE_KEY + id);
    }
}
