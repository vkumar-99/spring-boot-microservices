package com.javalang.bokstore.order.jobs;

import com.javalang.bokstore.order.domain.OrderEventService;
import java.time.Instant;

import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublishingJob {

    private static final Logger LOG = LoggerFactory.getLogger(OrderEventPublishingJob.class);
    private final OrderEventService orderEventService;

    public OrderEventPublishingJob(OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
    }

    @Scheduled(cron = "${order.publish-order-events-job-cron}")
    @SchedulerLock(name = "publishOrderEvents")
    public void publishOrderEvents() {
        LockAssert.assertLocked();
        LOG.info("Publishing order events at {}", Instant.now());
        orderEventService.publishOrderEvents();
    }
}
