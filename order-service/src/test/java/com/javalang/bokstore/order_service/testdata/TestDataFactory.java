package com.javalang.bokstore.order_service.testdata;

import static org.instancio.Select.field;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.javalang.bokstore.order_service.domain.models.Address;
import com.javalang.bokstore.order_service.domain.models.Customer;
import com.javalang.bokstore.order_service.domain.models.OrderItem;
import com.javalang.bokstore.order_service.domain.models.OrderRequest;
import org.instancio.Instancio;

public class TestDataFactory {
    static final List<String> VALID_COUNTIES = List.of("India", "Germany");
    static final Set<OrderItem> VALID_ORDER_ITEMS =
            Set.of(new OrderItem("P100", "Product 1", new BigDecimal("25.50"), 1));
    static final Set<OrderItem> INVALID_ORDER_ITEMS =
            Set.of(new OrderItem("ABCD", "Product 1", new BigDecimal("25.50"), 1));

    public static OrderRequest createValidOrderRequest() {
        return Instancio.of(OrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .set(field(OrderRequest::items), VALID_ORDER_ITEMS)
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES))
                .create();
    }

    public static OrderRequest createOrderRequestWithInvalidCustomer() {
        return Instancio.of(OrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
                .set(field(Customer::phone), "")
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES))
                .set(field(OrderRequest::items), VALID_ORDER_ITEMS)
                .create();
    }

    public static OrderRequest createOrderRequestWithInvalidDeliveryAddress() {
        return Instancio.of(OrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
                .set(field(Address::country), "")
                .set(field(OrderRequest::items), VALID_ORDER_ITEMS)
                .create();
    }

    public static OrderRequest createOrderRequestWithNoItems() {
        return Instancio.of(OrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES))
                .set(field(OrderRequest ::items), Set.of())
                .create();
    }
}
