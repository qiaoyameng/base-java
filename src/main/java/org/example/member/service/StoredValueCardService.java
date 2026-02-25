package org.example.member.service;

import org.example.member.entity.RechargeRecord;
import org.example.member.entity.StoredValueCard;
import org.example.member.repository.RechargeRecordRepository;
import org.example.member.repository.StoredValueCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StoredValueCardService {
    
    private final StoredValueCardRepository cardRepository;
    private final RechargeRecordRepository rechargeRecordRepository;

    public StoredValueCardService(StoredValueCardRepository cardRepository, 
                                  RechargeRecordRepository rechargeRecordRepository) {
        this.cardRepository = cardRepository;
        this.rechargeRecordRepository = rechargeRecordRepository;
    }

    public List<StoredValueCard> findByMemberId(Long memberId) {
        return cardRepository.findByMemberId(memberId);
    }

    public Optional<StoredValueCard> findById(Long id) {
        return cardRepository.findById(id);
    }

    public Optional<StoredValueCard> findByCardNo(String cardNo) {
        return cardRepository.findByCardNo(cardNo);
    }

    @Transactional
    public StoredValueCard createCard(Long memberId) {
        StoredValueCard card = new StoredValueCard();
        card.setMemberId(memberId);
        card.setCardNo(generateCardNo());
        card.setBalance(BigDecimal.ZERO);
        card.setTotalRecharge(BigDecimal.ZERO);
        card.setTotalConsumed(BigDecimal.ZERO);
        card.setStatus(1);
        return cardRepository.save(card);
    }

    @Transactional
    public boolean recharge(Long cardId, BigDecimal amount, BigDecimal bonusAmount, 
                           String paymentMethod, String paymentNo) {
        return cardRepository.findById(cardId).map(card -> {
            card.setBalance(card.getBalance().add(amount).add(bonusAmount));
            card.setTotalRecharge(card.getTotalRecharge().add(amount));
            cardRepository.save(card);
            
            RechargeRecord record = new RechargeRecord();
            record.setMemberId(card.getMemberId());
            record.setCardId(cardId);
            record.setAmount(amount);
            record.setBonusAmount(bonusAmount);
            record.setPaymentMethod(paymentMethod);
            record.setPaymentNo(paymentNo);
            record.setStatus(1);
            rechargeRecordRepository.save(record);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean consume(Long cardId, BigDecimal amount, Long orderId) {
        return cardRepository.findById(cardId).map(card -> {
            if (card.getBalance().compareTo(amount) < 0) {
                return false;
            }
            card.setBalance(card.getBalance().subtract(amount));
            card.setTotalConsumed(card.getTotalConsumed().add(amount));
            cardRepository.save(card);
            return true;
        }).orElse(false);
    }

    public List<RechargeRecord> getRechargeRecords(Long memberId) {
        return rechargeRecordRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
    }

    private String generateCardNo() {
        return "SV" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}
