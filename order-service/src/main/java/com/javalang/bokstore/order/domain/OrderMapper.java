package com.javalang.bokstore.order.domain;

import com.javalang.bokstore.order.domain.models.CreateOrderRequest;
import com.javalang.bokstore.order.domain.models.OrderInfo;
import com.javalang.bokstore.order.domain.models.OrderItem;
import com.javalang.bokstore.order.domain.models.OrderStatus;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderEntity convertToEntity(CreateOrderRequest createOrderRequest) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderNumber(UUID.randomUUID().toString());
        entity.setStatus(OrderStatus.NEW);
        entity.setCustomer(createOrderRequest.customer());
        entity.setDeliveryAddress(createOrderRequest.deliveryAddress());
        Set<OrderItemEntity> orderItems = new HashSet<>();
        for (OrderItem item : createOrderRequest.items()) {
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

    public static OrderInfo convertToOrderInfo(OrderEntity order) {
        Set<OrderItem> orderItems = order.getItems().stream()
                .map(item -> new OrderItem(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());

        return new OrderInfo(
                order.getOrderNumber(),
                order.getUserName(),
                orderItems,
                order.getCustomer(),
                order.getDeliveryAddress(),
                order.getStatus(),
                order.getComments(),
                order.getCreatedAt());
    }
}
