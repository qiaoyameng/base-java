package org.example.record.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.order.entity.Order;
import org.example.order.entity.OrderItem;
import org.example.order.service.OrderService;
import org.example.record.entity.ServiceVoucher;
import org.example.record.repository.ServiceVoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ServiceVoucherService {
    
    private final ServiceVoucherRepository voucherRepository;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public ServiceVoucherService(ServiceVoucherRepository voucherRepository, 
                                OrderService orderService,
                                ObjectMapper objectMapper) {
        this.voucherRepository = voucherRepository;
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    public Optional<ServiceVoucher> findById(Long id) {
        return voucherRepository.findById(id);
    }

    public Optional<ServiceVoucher> findByVoucherNo(String voucherNo) {
        return voucherRepository.findByVoucherNo(voucherNo);
    }

    public List<ServiceVoucher> findByOrderId(Long orderId) {
        return voucherRepository.findByOrderId(orderId);
    }

    public List<ServiceVoucher> findByMemberId(Long memberId) {
        return voucherRepository.findByMemberId(memberId);
    }

    @Transactional
    public ServiceVoucher generateVoucher(Long orderId) {
        Optional<Order> orderOpt = orderService.findById(orderId);
        if (orderOpt.isEmpty()) {
            return null;
        }
        
        Order order = orderOpt.get();
        List<OrderItem> items = orderService.getOrderItems(orderId);
        
        ServiceVoucher voucher = new ServiceVoucher();
        voucher.setVoucherNo(generateVoucherNo());
        voucher.setOrderId(orderId);
        voucher.setMemberId(order.getMemberId());
        voucher.setStoreId(order.getStoreId());
        voucher.setVoucherType("CONSUMPTION");
        voucher.setTotalAmount(order.getTotalAmount());
        voucher.setDiscountAmount(order.getDiscountAmount());
        voucher.setPayAmount(order.getPayAmount());
        voucher.setPaymentMethod(order.getPaymentMethod());
        voucher.setServiceDetails(generateServiceDetails(items));
        voucher.setGenerateTime(LocalDateTime.now());
        voucher.setStatus(1);
        
        return voucherRepository.save(voucher);
    }

    @Transactional
    public ServiceVoucher generateServiceDetailSheet(Long orderId) {
        Optional<Order> orderOpt = orderService.findById(orderId);
        if (orderOpt.isEmpty()) {
            return null;
        }
        
        Order order = orderOpt.get();
        List<OrderItem> items = orderService.getOrderItems(orderId);
        
        ServiceVoucher voucher = new ServiceVoucher();
        voucher.setVoucherNo(generateVoucherNo());
        voucher.setOrderId(orderId);
        voucher.setMemberId(order.getMemberId());
        voucher.setStoreId(order.getStoreId());
        voucher.setVoucherType("DETAIL_SHEET");
        voucher.setServiceDetails(generateDetailSheet(items));
        voucher.setGenerateTime(LocalDateTime.now());
        voucher.setStatus(1);
        
        return voucherRepository.save(voucher);
    }

    private String generateVoucherNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "VCH" + dateStr + randomStr;
    }

    private String generateServiceDetails(List<OrderItem> items) {
        try {
            List<Map<String, Object>> details = new ArrayList<>();
            for (OrderItem item : items) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("serviceName", item.getServiceName());
                detail.put("category", item.getCategory() != null ? item.getCategory().name() : null);
                detail.put("quantity", item.getQuantity());
                detail.put("unitPrice", item.getUnitPrice());
                detail.put("subtotal", item.getSubtotal());
                detail.put("clothesName", item.getClothesName());
                details.add(detail);
            }
            return objectMapper.writeValueAsString(details);
        } catch (Exception e) {
            return "[]";
        }
    }

    private String generateDetailSheet(List<OrderItem> items) {
        try {
            List<Map<String, Object>> details = new ArrayList<>();
            for (OrderItem item : items) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("serviceName", item.getServiceName());
                detail.put("clothesName", item.getClothesName());
                detail.put("clothesColor", item.getClothesColor());
                detail.put("clothesBrand", item.getClothesBrand());
                detail.put("specialNote", item.getSpecialNote());
                detail.put("quantity", item.getQuantity());
                detail.put("status", item.getItemStatus());
                details.add(detail);
            }
            return objectMapper.writeValueAsString(details);
        } catch (Exception e) {
            return "[]";
        }
    }
}
