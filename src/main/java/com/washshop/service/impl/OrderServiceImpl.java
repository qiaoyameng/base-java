package com.washshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.washshop.dto.OrderCancelDTO;
import com.washshop.dto.OrderDTO;
import com.washshop.dto.OrderItemDTO;
import com.washshop.dto.OrderStatusUpdateDTO;
import com.washshop.entity.Order;
import com.washshop.entity.OrderItem;
import com.washshop.entity.Store;
import com.washshop.entity.WashService;
import com.washshop.enums.DeliveryType;
import com.washshop.enums.OrderStatus;
import com.washshop.mapper.OrderItemMapper;
import com.washshop.mapper.OrderMapper;
import com.washshop.mapper.StoreMapper;
import com.washshop.mapper.WashServiceMapper;
import com.washshop.service.OrderService;
import com.washshop.vo.OrderItemVO;
import com.washshop.vo.OrderVO;
import com.washshop.vo.PageVO;
import com.washshop.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private WashServiceMapper washServiceMapper;

    @Autowired
    private StoreMapper storeMapper;

    private static final DateTimeFormatter ORDER_NO_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderVO> createOrder(Long userId, OrderDTO dto) {
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setStoreId(dto.getStoreId());
        order.setDeliveryType(dto.getDeliveryType());
        order.setPickupAddress(dto.getPickupAddress());
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setPickupTime(dto.getPickupTime());
        order.setDeliveryTime(dto.getDeliveryTime());
        order.setContactName(dto.getContactName());
        order.setContactPhone(dto.getContactPhone());
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT.getCode());
        order.setRemark(dto.getRemark());
        order.setReminderCount(0);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO itemDTO : dto.getItems()) {
            WashService service = washServiceMapper.selectById(itemDTO.getServiceId());
            if (service == null || service.getStatus() != 1) {
                return Result.error("服务不存在或已下架");
            }

            OrderItem item = new OrderItem();
            item.setServiceId(itemDTO.getServiceId());
            item.setServiceName(service.getServiceName());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(service.getPrice());
            item.setTotalPrice(service.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            item.setDescription(itemDTO.getDescription());
            item.setItemStatus("PENDING");
            orderItems.add(item);

            totalAmount = totalAmount.add(item.getTotalPrice());
        }

        BigDecimal deliveryFee = dto.getDeliveryType().equals(DeliveryType.DOOR_TO_DOOR.getCode()) ? new BigDecimal("10.00") : BigDecimal.ZERO;
        order.setTotalAmount(totalAmount);
        order.setDeliveryFee(deliveryFee);
        order.setActualAmount(totalAmount.add(deliveryFee));

        save(order);

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }

        return Result.success("下单成功", convertToVO(order));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> payOrder(Long orderId, Integer payType) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getOrderStatus().equals(OrderStatus.PENDING_PAYMENT.getCode())) {
            return Result.error("订单状态不正确");
        }

        order.setOrderStatus(OrderStatus.PENDING_ACCEPT.getCode());
        order.setPayType(payType);
        order.setPayTime(LocalDateTime.now());
        order.setPayNo("PAY" + System.currentTimeMillis());
        updateById(order);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> acceptOrder(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getOrderStatus().equals(OrderStatus.PENDING_ACCEPT.getCode())) {
            return Result.error("订单状态不正确");
        }

        order.setOrderStatus(OrderStatus.WASHING.getCode());
        order.setConfirmTime(LocalDateTime.now());
        updateById(order);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> startWash(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        order.setStartWashTime(LocalDateTime.now());
        updateById(order);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> finishWash(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        order.setFinishWashTime(LocalDateTime.now());
        updateById(order);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> readyForPickup(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        order.setOrderStatus(OrderStatus.READY.getCode());
        order.setReadyTime(LocalDateTime.now());
        updateById(order);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> startDelivery(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        order.setOrderStatus(OrderStatus.DELIVERING.getCode());
        order.setDeliverTime(LocalDateTime.now());
        updateById(order);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> completeOrder(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        order.setOrderStatus(OrderStatus.COMPLETED.getCode());
        order.setCompleteTime(LocalDateTime.now());
        updateById(order);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> cancelOrder(Long userId, Long orderId, OrderCancelDTO dto) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权操作此订单");
        }
        if (!order.getOrderStatus().equals(OrderStatus.PENDING_PAYMENT.getCode()) &&
            !order.getOrderStatus().equals(OrderStatus.PENDING_ACCEPT.getCode())) {
            return Result.error("订单状态不允许取消");
        }

        order.setOrderStatus(OrderStatus.CANCELLED.getCode());
        order.setCancelReason(dto.getCancelReason());
        updateById(order);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> applyRefund(Long orderId, String reason) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getOrderStatus().equals(OrderStatus.PENDING_ACCEPT.getCode()) &&
            !order.getOrderStatus().equals(OrderStatus.WASHING.getCode())) {
            return Result.error("订单状态不允许退款");
        }

        order.setOrderStatus(OrderStatus.REFUNDING.getCode());
        order.setCancelReason(reason);
        updateById(order);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> approveRefund(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getOrderStatus().equals(OrderStatus.REFUNDING.getCode())) {
            return Result.error("订单状态不正确");
        }

        order.setOrderStatus(OrderStatus.REFUNDED.getCode());
        updateById(order);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> rejectRefund(Long orderId, String reason) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getOrderStatus().equals(OrderStatus.REFUNDING.getCode())) {
            return Result.error("订单状态不正确");
        }

        order.setOrderStatus(OrderStatus.WASHING.getCode());
        updateById(order);

        return Result.success();
    }

    @Override
    public Result<OrderVO> getOrderDetail(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(convertToVO(order));
    }

    @Override
    public Result<PageVO<OrderVO>> getUserOrders(Long userId, Integer status, Long current, Long size) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null) {
            wrapper.eq(Order::getOrderStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> page = new Page<>(current, size);
        page(page, wrapper);

        List<OrderVO> voList = page.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        PageVO<OrderVO> pageVO = new PageVO<>(page.getTotal(), current, size, voList);
        return Result.success(pageVO);
    }

    @Override
    public Result<PageVO<OrderVO>> getStoreOrders(Long storeId, Integer status, Long current, Long size) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getStoreId, storeId);
        if (status != null) {
            wrapper.eq(Order::getOrderStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> page = new Page<>(current, size);
        page(page, wrapper);

        List<OrderVO> voList = page.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        PageVO<OrderVO> pageVO = new PageVO<>(page.getTotal(), current, size, voList);
        return Result.success(pageVO);
    }

    @Override
    public Result<List<OrderVO>> getOrdersByStatus(Integer status) {
        List<Order> orders = baseMapper.selectByStatus(status);
        List<OrderVO> voList = orders.stream().map(this::convertToVO).collect(Collectors.toList());
        return Result.success(voList);
    }

    @Override
    public void sendOrderReminder() {
        LocalDateTime reminderTime = LocalDateTime.now().plusMinutes(30);
        List<Order> orders = baseMapper.selectReadyOrdersForReminder(reminderTime);

        for (Order order : orders) {
            System.out.println("发送取件提醒给用户: " + order.getUserId() + ", 订单号: " + order.getOrderNo());
            baseMapper.incrementReminderCount(order.getId());
        }
    }

    private String generateOrderNo() {
        return "WS" + LocalDateTime.now().format(ORDER_NO_FORMATTER) + String.format("%04d", (int)(Math.random() * 10000));
    }

    private OrderVO convertToVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);

        OrderStatus status = OrderStatus.fromCode(order.getOrderStatus());
        if (status != null) {
            vo.setOrderStatusName(status.getDesc());
        }

        DeliveryType deliveryType = DeliveryType.fromCode(order.getDeliveryType());
        if (deliveryType != null) {
            vo.setDeliveryTypeName(deliveryType.getDesc());
        }

        Store store = storeMapper.selectById(order.getStoreId());
        if (store != null) {
            vo.setStoreName(store.getStoreName());
        }

        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getId());
        List<OrderItemVO> itemVOs = items.stream().map(item -> {
            OrderItemVO itemVO = new OrderItemVO();
            BeanUtils.copyProperties(item, itemVO);
            return itemVO;
        }).collect(Collectors.toList());
        vo.setItems(itemVOs);

        return vo;
    }
}
