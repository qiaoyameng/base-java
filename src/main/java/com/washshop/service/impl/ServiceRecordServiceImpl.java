package com.washshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.washshop.dto.ReviewDTO;
import com.washshop.entity.Order;
import com.washshop.entity.OrderItem;
import com.washshop.entity.ServiceRecord;
import com.washshop.entity.Store;
import com.washshop.mapper.OrderItemMapper;
import com.washshop.mapper.OrderMapper;
import com.washshop.mapper.ServiceRecordMapper;
import com.washshop.mapper.StoreMapper;
import com.washshop.service.ServiceRecordService;
import com.washshop.vo.Result;
import com.washshop.vo.ServiceRecordVO;
import com.washshop.vo.VoucherItemVO;
import com.washshop.vo.VoucherVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceRecordServiceImpl extends ServiceImpl<ServiceRecordMapper, ServiceRecord> implements ServiceRecordService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private StoreMapper storeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<ServiceRecord> createRecord(Long orderId, Long orderItemId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        OrderItem item = orderItemMapper.selectById(orderItemId);
        if (item == null) {
            return Result.error("订单项不存在");
        }

        ServiceRecord record = new ServiceRecord();
        record.setOrderId(orderId);
        record.setOrderItemId(orderItemId);
        record.setUserId(order.getUserId());
        record.setStoreId(order.getStoreId());
        record.setServiceId(item.getServiceId());
        record.setServiceName(item.getServiceName());
        record.setSignInTime(LocalDateTime.now());

        save(record);
        return Result.success(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateWashTimes(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        List<ServiceRecord> records = baseMapper.selectByOrderId(orderId);
        for (ServiceRecord record : records) {
            if (order.getStartWashTime() != null) {
                record.setStartWashTime(order.getStartWashTime());
            }
            if (order.getFinishWashTime() != null) {
                record.setFinishWashTime(order.getFinishWashTime());
            }
            if (order.getCompleteTime() != null) {
                record.setDeliverTime(order.getCompleteTime());
            }

            if (record.getStartWashTime() != null && record.getFinishWashTime() != null) {
                long minutes = ChronoUnit.MINUTES.between(record.getStartWashTime(), record.getFinishWashTime());
                record.setActualDuration((int) minutes);
            }

            updateById(record);
        }

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> submitReview(Long userId, Long recordId, ReviewDTO dto) {
        ServiceRecord record = getById(recordId);
        if (record == null) {
            return Result.error("服务记录不存在");
        }
        if (!record.getUserId().equals(userId)) {
            return Result.error("无权评价此记录");
        }
        if (record.getRating() != null) {
            return Result.error("已经评价过了");
        }

        record.setRating(dto.getRating());
        record.setReviewContent(dto.getReviewContent());
        record.setReviewImages(dto.getReviewImages());
        record.setReviewTime(LocalDateTime.now());

        updateById(record);
        return Result.success();
    }

    @Override
    public Result<ServiceRecordVO> getRecordById(Long id) {
        ServiceRecord record = getById(id);
        if (record == null) {
            return Result.error("服务记录不存在");
        }
        return Result.success(convertToVO(record));
    }

    @Override
    public Result<List<ServiceRecordVO>> getRecordsByOrderId(Long orderId) {
        List<ServiceRecord> records = baseMapper.selectByOrderId(orderId);
        List<ServiceRecordVO> voList = records.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    @Override
    public Result<List<ServiceRecordVO>> getRecordsByUserId(Long userId) {
        List<ServiceRecord> records = baseMapper.selectByUserId(userId);
        List<ServiceRecordVO> voList = records.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<VoucherVO> generateVoucher(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        String voucherNo = "VC" + System.currentTimeMillis();
        order.setVoucherNo(voucherNo);
        orderMapper.updateById(order);

        List<ServiceRecord> records = baseMapper.selectByOrderId(orderId);
        for (ServiceRecord record : records) {
            record.setVoucherNo(voucherNo);
            StringBuilder content = new StringBuilder();
            content.append("服务名称：").append(record.getServiceName()).append("\n");
            content.append("洗护时长：").append(record.getActualDuration()).append("分钟\n");
            content.append("完成时间：").append(record.getDeliverTime()).append("\n");
            record.setVoucherContent(content.toString());
            updateById(record);
        }

        return Result.success(buildVoucherVO(order));
    }

    @Override
    public Result<VoucherVO> getVoucherByNo(String voucherNo) {
        ServiceRecord record = baseMapper.selectByVoucherNo(voucherNo);
        if (record == null) {
            return Result.error("凭证不存在");
        }

        Order order = orderMapper.selectById(record.getOrderId());
        if (order == null) {
            return Result.error("订单不存在");
        }

        return Result.success(buildVoucherVO(order));
    }

    private ServiceRecordVO convertToVO(ServiceRecord record) {
        ServiceRecordVO vo = new ServiceRecordVO();
        BeanUtils.copyProperties(record, vo);

        Store store = storeMapper.selectById(record.getStoreId());
        if (store != null) {
            vo.setStoreName(store.getStoreName());
        }

        return vo;
    }

    private VoucherVO buildVoucherVO(Order order) {
        VoucherVO vo = new VoucherVO();
        vo.setVoucherNo(order.getVoucherNo());
        vo.setOrderNo(order.getOrderNo());

        Store store = storeMapper.selectById(order.getStoreId());
        if (store != null) {
            vo.setStoreName(store.getStoreName());
        }

        vo.setContactName(order.getContactName());
        vo.setContactPhone(order.getContactPhone());
        vo.setCompleteTime(order.getCompleteTime());
        vo.setActualAmount(order.getActualAmount());

        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
        List<VoucherItemVO> itemVOs = new ArrayList<>();
        for (OrderItem item : items) {
            VoucherItemVO itemVO = new VoucherItemVO();
            itemVO.setServiceName(item.getServiceName());
            itemVO.setQuantity(item.getQuantity());
            itemVO.setUnitPrice(item.getUnitPrice());
            itemVO.setTotalPrice(item.getTotalPrice());
            itemVOs.add(itemVO);
        }
        vo.setItems(itemVOs);

        vo.setQrCode("QR_" + order.getVoucherNo());

        return vo;
    }
}
