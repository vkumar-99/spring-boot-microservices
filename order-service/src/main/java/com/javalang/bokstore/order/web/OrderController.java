package com.javalang.bokstore.order.web;

import com.javalang.bokstore.order.domain.OrderNotFoundException;
import com.javalang.bokstore.order.domain.OrderService;
import com.javalang.bokstore.order.domain.SecurityService;
import com.javalang.bokstore.order.domain.models.CreateOrderRequest;
import com.javalang.bokstore.order.domain.models.CreateOrderResponse;
import com.javalang.bokstore.order.domain.models.OrderInfo;
import com.javalang.bokstore.order.domain.models.OrderSummary;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
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
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String userName = securityService.getLoginUsername();
        logger.info("LoggedIn username {}", userName);
        return orderService.createOrder(userName, request);
    }

    @GetMapping
    List<OrderSummary> getOrders() {
        String userName = securityService.getLoginUsername();
        logger.info("Fetching orders for {}", userName);
        return orderService.findOrders(userName);
    }

    @GetMapping(value = "/{orderNumber}")
    OrderInfo getOrdersByOrderNumber(@PathVariable(name = "orderNumber") String orderNumber) {
        String userName = securityService.getLoginUsername();
        logger.info("Fetching orders for {}", userName);
        return orderService
                .findUserOrder(userName, orderNumber)
                .orElseThrow(() -> OrderNotFoundException.forOrderNumber(orderNumber));
    }
}
