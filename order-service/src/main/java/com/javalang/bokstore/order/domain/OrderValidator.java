package com.javalang.bokstore.order.domain;

import com.javalang.bokstore.order.client.CatalogServiceClient;
import com.javalang.bokstore.order.client.model.Product;
import com.javalang.bokstore.order.domain.models.CreateOrderRequest;
import com.javalang.bokstore.order.domain.models.OrderItem;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderValidator.class);
    private final CatalogServiceClient restClient;

    public OrderValidator(CatalogServiceClient restClient) {
        this.restClient = restClient;
    }

    public void validateOrder(CreateOrderRequest createOrderRequest) {
        Set<OrderItem> items = createOrderRequest.items();
        for (OrderItem item : items) {
            Product product = restClient
                    .getProductByCode(item.code())
                    .orElseThrow(() -> InvalidOrderException.forInvalidProductCode(item.code()));
            if (item.price().compareTo(product.price()) != 0) {
                LOGGER.error(
                        "Product price mismatch, Actual price {}, Received price {}", product.price(), item.price());
                throw InvalidOrderException.forProductPriceMismatch(item.code());
            }
        }
    }
}
