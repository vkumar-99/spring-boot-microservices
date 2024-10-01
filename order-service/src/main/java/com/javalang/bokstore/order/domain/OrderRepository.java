package com.javalang.bokstore.order.domain;

import com.javalang.bokstore.order.domain.models.OrderStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus status) {
        OrderEntity entity = this.findByOrderNumber(orderNumber).orElseThrow();
        entity.setStatus(status);
        this.save(entity);
    }
}
