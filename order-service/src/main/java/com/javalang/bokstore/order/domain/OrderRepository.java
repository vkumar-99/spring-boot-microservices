package com.javalang.bokstore.order.domain;

import com.javalang.bokstore.order.domain.models.OrderStatus;
import com.javalang.bokstore.order.domain.models.OrderSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus status) {
        OrderEntity entity = this.findByOrderNumber(orderNumber).orElseThrow();
        entity.setStatus(status);
        this.save(entity);
    }

    @Query(
            """
            select new com.javalang.bokstore.order.domain.models.OrderSummary(o.orderNumber, o.status)
            from OrderEntity o
            where o.userName = :userName
            """)
    List<OrderSummary> findByUserName(String userName);

    @Query(
            """
            select o
            from OrderEntity o left join fetch o.items
            where o.userName = :userName and o.orderNumber = :orderNumber
            """)
    Optional<OrderEntity> findByUserNameAndOrderNumber(String userName, String orderNumber);
}
