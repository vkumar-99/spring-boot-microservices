package com.javalang.bokstore.order.jobs;

import com.javalang.bokstore.order.domain.OrderService;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingJob {

    private final OrderService orderService;

    public OrderProcessingJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${order.new-order-job-cron}")
    @SchedulerLock(name = "processNewOrders")
    public void processNewOrders() {
        LockAssert.assertLocked();
        orderService.processNewOrders();
    }
}
