package com.javalang.bokstore.order_service.domain;

import com.javalang.bokstore.order_service.domain.models.OrderRequest;
import com.javalang.bokstore.order_service.domain.models.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(String userName, OrderRequest orderRequest) {
        OrderEntity orderEntity = OrderMapper.convertToEntity(orderRequest);
        orderEntity.setUserName(userName);
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        LOGGER.info("Created order with orderId: {}", savedOrder.getOrderNumber());
        return new OrderResponse(savedOrder.getOrderNumber());
    }
}
