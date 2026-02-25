package com.washshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.washshop.dto.OrderCancelDTO;
import com.washshop.dto.OrderDTO;
import com.washshop.dto.OrderStatusUpdateDTO;
import com.washshop.entity.Order;
import com.washshop.vo.OrderVO;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;

import java.util.List;

public interface OrderService extends IService<Order> {

    Result<OrderVO> createOrder(Long userId, OrderDTO dto);

    Result<Void> payOrder(Long orderId, Integer payType);

    Result<Void> acceptOrder(Long orderId);

    Result<Void> startWash(Long orderId);

    Result<Void> finishWash(Long orderId);

    Result<Void> readyForPickup(Long orderId);

    Result<Void> startDelivery(Long orderId);

    Result<Void> completeOrder(Long orderId);

    Result<Void> cancelOrder(Long userId, Long orderId, OrderCancelDTO dto);

    Result<Void> applyRefund(Long orderId, String reason);

    Result<Void> approveRefund(Long orderId);

    Result<Void> rejectRefund(Long orderId, String reason);

    Result<OrderVO> getOrderDetail(Long orderId);

    Result<PageVO<OrderVO>> getUserOrders(Long userId, Integer status, Long current, Long size);

    Result<PageVO<OrderVO>> getStoreOrders(Long storeId, Integer status, Long current, Long size);

    Result<List<OrderVO>> getOrdersByStatus(Integer status);

    void sendOrderReminder();
}
