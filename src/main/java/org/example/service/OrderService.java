package org.example.service;

import org.example.dto.CreateOrderDTO;
import org.example.dto.OrderDTO;
import org.example.dto.OrderQueryDTO;
import org.example.common.PageResult;

public interface OrderService {

    OrderDTO createOrder(CreateOrderDTO createOrderDTO);

    OrderDTO getOrderById(Long id);

    OrderDTO getOrderByNo(String orderNo);

    PageResult<OrderDTO> listOrders(OrderQueryDTO queryDTO);

    PageResult<OrderDTO> listCustomerOrders(Long customerId, Integer pageNum, Integer pageSize);

    void payOrder(Long orderId);

    void prepareOrder(Long orderId);

    void shipOrder(Long orderId, String trackingNo);

    void completeOrder(Long orderId);

    void cancelOrder(Long orderId, String reason);

    void applyRefund(Long orderId, String reason);

    void approveRefund(Long orderId);

    void rejectRefund(Long orderId, String reason);
}
