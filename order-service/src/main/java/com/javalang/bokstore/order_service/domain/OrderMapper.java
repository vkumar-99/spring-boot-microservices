package com.javalang.bokstore.order_service.domain;

import com.javalang.bokstore.order_service.domain.models.OrderItem;
import com.javalang.bokstore.order_service.domain.models.OrderRequest;
import com.javalang.bokstore.order_service.domain.models.OrderStatus;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OrderMapper {

    public static OrderEntity convertToEntity(OrderRequest orderRequest) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderNumber(UUID.randomUUID().toString());
        entity.setStatus(OrderStatus.NEW);
        entity.setCustomer(orderRequest.customer());
        entity.setDeliveryAddress(orderRequest.deliveryAddress());
        Set<OrderItemEntity> orderItems = new HashSet<>();
        for (OrderItem item : orderRequest.items()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setCode(item.code());
            orderItem.setName(item.name());
            orderItem.setPrice(item.price());
            orderItem.setQuantity(item.quantity());
            orderItem.setOrder(entity);
            orderItems.add(orderItem);
        }
        entity.setItems(orderItems);
        return entity;
    }
}
