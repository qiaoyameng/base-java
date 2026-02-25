package com.washshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.washshop.dto.ReviewDTO;
import com.washshop.entity.ServiceRecord;
import com.washshop.vo.Result;
import com.washshop.vo.ServiceRecordVO;
import com.washshop.vo.VoucherVO;

import java.util.List;

public interface ServiceRecordService extends IService<ServiceRecord> {

    Result<ServiceRecord> createRecord(Long orderId, Long orderItemId);

    Result<Void> updateWashTimes(Long orderId);

    Result<Void> submitReview(Long userId, Long recordId, ReviewDTO dto);

    Result<ServiceRecordVO> getRecordById(Long id);

    Result<List<ServiceRecordVO>> getRecordsByOrderId(Long orderId);

    Result<List<ServiceRecordVO>> getRecordsByUserId(Long userId);

    Result<VoucherVO> generateVoucher(Long orderId);

    Result<VoucherVO> getVoucherByNo(String voucherNo);
}
