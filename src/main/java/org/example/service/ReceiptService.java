package org.example.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.example.entity.OrderItem;
import org.example.entity.Orders;
import org.example.entity.Receipt;
import org.example.repository.OrderRepository;
import org.example.repository.ReceiptRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Receipt generateReceipt(Orders order) {
        Receipt existing = receiptRepository.findByOrderId(order.getId()).orElse(null);
        if (existing != null) {
            return existing;
        }

        Receipt receipt = new Receipt();
        receipt.setReceiptNo("RCP" + IdUtil.getSnowflakeNextIdStr());
        receipt.setOrderId(order.getId());
        receipt.setOrderNo(order.getOrderNo());
        receipt.setCustomerId(order.getCustomerId());

        List<OrderItem> items = order.getOrderItems();
        Map<String, Object> itemsMap = new HashMap<>();
        for (OrderItem item : items) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("name", item.getProductName());
            itemData.put("spec", item.getProductSpec());
            itemData.put("quantity", item.getQuantity());
            itemData.put("unitPrice", item.getUnitPrice());
            itemData.put("subtotal", item.getSubtotal());
            itemsMap.put(String.valueOf(item.getId()), itemData);
        }
        receipt.setItems(JSONUtil.toJsonStr(itemsMap));

        receipt.setTotalAmount(order.getTotalAmount());
        receipt.setPaidAmount(order.getPaidAmount());
        receipt.setPaymentMethodName(order.getPaymentMethod() != null ? order.getPaymentMethod().getDesc() : "");
        receipt.setRemark(order.getRemark());
        receipt.setPaidTime(order.getPaymentTime());

        receipt.setQrCode("https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + receipt.getReceiptNo());

        return receiptRepository.save(receipt);
    }

    public Receipt findByReceiptNo(String receiptNo) {
        return receiptRepository.findByReceiptNo(receiptNo).orElse(null);
    }

    public Receipt findByOrderNo(String orderNo) {
        return receiptRepository.findByOrderNo(orderNo).orElse(null);
    }

    public Receipt findByOrderId(Long orderId) {
        return receiptRepository.findByOrderId(orderId).orElse(null);
    }

    public Page<Receipt> findByCustomerId(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return receiptRepository.findByCustomerId(customerId, pageable);
    }

    @Transactional
    public Receipt generateReceiptByOrderNo(String orderNo) {
        Orders order = orderRepository.findByOrderNo(orderNo).orElse(null);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        return generateReceipt(order);
    }
}
