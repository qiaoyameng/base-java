package org.example.order.controller;

import org.example.common.Result;
import org.example.order.entity.Order;
import org.example.order.entity.OrderItem;

import java.util.List;

public class OrderCreateRequest {
    private Order order;
    private List<OrderItem> items;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
