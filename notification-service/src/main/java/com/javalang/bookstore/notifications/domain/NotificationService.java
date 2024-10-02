package com.javalang.bookstore.notifications.domain;

import com.javalang.bookstore.notifications.config.ApplicationProperties;
import com.javalang.bookstore.notifications.domain.models.OrderCancelledEvent;
import com.javalang.bookstore.notifications.domain.models.OrderCreatedEvent;
import com.javalang.bookstore.notifications.domain.models.OrderDeliveredEvent;
import com.javalang.bookstore.notifications.domain.models.OrderErrorEvent;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender emailSender;
    private final ApplicationProperties properties;
    private final OrderEventRepository orderEventRepository;

    public NotificationService(
            JavaMailSender emailSender, ApplicationProperties properties, OrderEventRepository orderEventRepository) {
        this.emailSender = emailSender;
        this.properties = properties;
        this.orderEventRepository = orderEventRepository;
    }

    public void sendOrderCreatedNotification(OrderCreatedEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Duplicate eventId {} received.Skipping the process.", event.eventId());
            return;
        }
        String message =
                """
                ===================================================
                Order Created Notification
                ----------------------------------------------------
                Dear %s,
                Your order with orderNumber: %s has been created successfully.

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Created Notification", message);
        saveOrderEventData(event.eventId());
    }

    public void sendOrderDeliveredNotification(OrderDeliveredEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Duplicate eventId {} received.Skipping the process.", event.eventId());
            return;
        }
        String message =
                """
                ===================================================
                Order Delivered Notification
                ----------------------------------------------------
                Dear %s,
                Your order with orderNumber: %s has been delivered successfully.

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Delivered Notification", message);
        saveOrderEventData(event.eventId());
    }

    public void sendOrderCancelledNotification(OrderCancelledEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Duplicate eventId {} received.Skipping the process.", event.eventId());
            return;
        }
        String message =
                """
                ===================================================
                Order Cancelled Notification
                ----------------------------------------------------
                Dear %s,
                Your order with orderNumber: %s has been cancelled.
                Reason: %s

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(event.customer().name(), event.orderNumber(), event.reason());
        log.info("\n{}", message);
        sendEmail(event.customer().email(), "Order Cancelled Notification", message);
        saveOrderEventData(event.eventId());
    }

    public void sendOrderErrorEventNotification(OrderErrorEvent event) {
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Duplicate eventId {} received.Skipping the process.", event.eventId());
            return;
        }
        String message =
                """
                ===================================================
                Order Processing Failure Notification
                ----------------------------------------------------
                Hi %s,
                The order processing failed for orderNumber: %s.
                Reason: %s

                Thanks,
                BookStore Team
                ===================================================
                """
                        .formatted(properties.supportEmail(), event.orderNumber(), event.reason());
        log.info("\n{}", message);
        sendEmail(properties.supportEmail(), "Order Processing Failure Notification", message);
        saveOrderEventData(event.eventId());
    }

    private void sendEmail(String recipient, String subject, String content) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(properties.supportEmail());
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(content);
            emailSender.send(mimeMessage);
            log.info("Email sent to: {}", recipient);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }

    private void saveOrderEventData(String eventId) {
        OrderEventEntity orderEventEntity = new OrderEventEntity(eventId);
        orderEventRepository.save(orderEventEntity);
    }
}
