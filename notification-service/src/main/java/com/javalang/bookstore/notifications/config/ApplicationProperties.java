package com.javalang.bookstore.notifications.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notifications")
public record ApplicationProperties(
        String supportEmail,
        String orderEventExchange,
        String newOrderQueue,
        String deliveredOrderQueue,
        String cancelledOrderQueue,
        String errorOrderQueue) {}
