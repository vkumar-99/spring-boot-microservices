package com.javalang.bokstore.order_service.web;

import com.javalang.bokstore.order_service.domain.OrderService;
import com.javalang.bokstore.order_service.domain.SecurityService;
import com.javalang.bokstore.order_service.domain.models.OrderRequest;
import com.javalang.bokstore.order_service.domain.models.OrderResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final SecurityService securityService;

    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        String userName = securityService.getLoginUsername();
        logger.info("Logged-in username {}", userName);
        return orderService.createOrder(userName, request);
    }
}
