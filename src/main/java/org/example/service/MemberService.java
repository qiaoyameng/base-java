package org.example.service;

import org.example.dto.*;
import org.example.common.PageResult;
import org.example.enums.MemberLevel;

import java.math.BigDecimal;
import java.util.List;

public interface MemberService {

    MemberDTO registerMember(MemberDTO memberDTO);

    MemberDTO updateMember(Long id, MemberDTO memberDTO);

    MemberDTO getMemberById(Long id);

    MemberDTO getMemberByPhone(String phone);

    PageResult<MemberDTO> listMembers(Integer pageNum, Integer pageSize);

    PageResult<MemberDTO> listMembersByLevel(MemberLevel level, Integer pageNum, Integer pageSize);

    void addPoints(Long memberId, Integer points);

    void deductPoints(Long memberId, Long points);

    BigDecimal calculatePointsDiscount(Long memberId, Long points);

    StoredValueTransactionDTO rechargeStoredValue(RechargeDTO rechargeDTO);

    StoredValueTransactionDTO consumeStoredValue(Long memberId, BigDecimal amount, Long orderId, String orderNo);

    PageResult<StoredValueTransactionDTO> listStoredValueTransactions(Long memberId, Integer pageNum, Integer pageSize);

    PageResult<PointsTransactionDTO> listPointsTransactions(Long memberId, Integer pageNum, Integer pageSize);

    void upgradeMemberLevel(Long memberId);

    MemberGiftDTO createMemberGift(MemberGiftDTO dto);

    List<MemberGiftDTO> listMemberGifts();

    List<MemberGiftDTO> listMemberGiftsByLevel(MemberLevel level);

    void claimGift(Long memberId, Long giftId);
}
