package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.StoredCardRequest;
import org.example.dto.StoredCardResponse;
import org.example.entity.Customer;
import org.example.entity.StoredCard;
import org.example.entity.StoredCardTransaction;
import org.example.exception.BusinessException;
import org.example.repository.CustomerRepository;
import org.example.repository.StoredCardRepository;
import org.example.repository.StoredCardTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoredCardService {
    private final StoredCardRepository storedCardRepository;
    private final StoredCardTransactionRepository storedCardTransactionRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public StoredCardResponse createCard(StoredCardRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new BusinessException("客户不存在"));

        StoredCard card = new StoredCard();
        card.setCustomer(customer);
        card.setCardNo(request.getCardNumber());
        card.setBalance(request.getBalance());
        card.setActive(true);
        StoredCard saved = storedCardRepository.save(card);
        return toResponse(saved);
    }

    @Transactional
    public StoredCardResponse recharge(Long cardId, BigDecimal amount, String description) {
        StoredCard card = storedCardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("储值卡不存在"));

        BigDecimal before = card.getBalance();
        card.setBalance(before.add(amount));
        StoredCard saved = storedCardRepository.save(card);

        StoredCardTransaction tx = new StoredCardTransaction();
        tx.setStoredCard(saved);
        tx.setType("RECHARGE");
        tx.setAmount(amount);
        tx.setBalanceBefore(before);
        tx.setBalanceAfter(saved.getBalance());
        tx.setDescription(description);
        storedCardTransactionRepository.save(tx);

        log.info("储值卡充值成功: cardId={}, amount={}", cardId, amount);
        return toResponse(saved);
    }

    @Transactional
    public StoredCardResponse deduct(Long cardId, BigDecimal amount, String description) {
        StoredCard card = storedCardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("储值卡不存在"));

        BigDecimal before = card.getBalance();
        if (before.compareTo(amount) < 0) {
            throw new BusinessException("储值卡余额不足");
        }
        card.setBalance(before.subtract(amount));
        StoredCard saved = storedCardRepository.save(card);

        StoredCardTransaction tx = new StoredCardTransaction();
        tx.setStoredCard(saved);
        tx.setType("CONSUME");
        tx.setAmount(amount);
        tx.setBalanceBefore(before);
        tx.setBalanceAfter(saved.getBalance());
        tx.setDescription(description);
        storedCardTransactionRepository.save(tx);

        log.info("储值卡扣费成功: cardId={}, amount={}", cardId, amount);
        return toResponse(saved);
    }

    public List<StoredCardResponse> getCardsByCustomer(Long customerId) {
        return storedCardRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCard(Long cardId) {
        StoredCard card = storedCardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException("储值卡不存在"));
        card.setActive(false);
        storedCardRepository.save(card);
    }

    private StoredCardResponse toResponse(StoredCard card) {
        StoredCardResponse resp = new StoredCardResponse();
        resp.setId(card.getId());
        resp.setCustomerId(card.getCustomer().getId());
        resp.setCardNumber(card.getCardNo());
        resp.setBalance(card.getBalance());
        resp.setEnabled(card.getActive());
        resp.setCreateTime(card.getCreateTime());
        return resp;
    }
}
