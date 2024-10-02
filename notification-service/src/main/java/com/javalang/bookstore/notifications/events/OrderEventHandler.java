package com.javalang.bookstore.notifications.events;

import com.javalang.bookstore.notifications.domain.NotificationService;
import com.javalang.bookstore.notifications.domain.models.OrderCancelledEvent;
import com.javalang.bookstore.notifications.domain.models.OrderCreatedEvent;
import com.javalang.bookstore.notifications.domain.models.OrderDeliveredEvent;
import com.javalang.bookstore.notifications.domain.models.OrderErrorEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final NotificationService notificationService;

    public OrderEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${notifications.new-order-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        notificationService.sendOrderCreatedNotification(event);
    }

    @RabbitListener(queues = "${notifications.delivered-order-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        notificationService.sendOrderDeliveredNotification(event);
    }

    @RabbitListener(queues = "${notifications.cancelled-order-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        notificationService.sendOrderCancelledNotification(event);
    }

    @RabbitListener(queues = "${notifications.error-order-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event) {
        notificationService.sendOrderErrorEventNotification(event);
    }
}
