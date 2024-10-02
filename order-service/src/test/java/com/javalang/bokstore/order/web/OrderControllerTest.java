package com.javalang.bokstore.order.web;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.javalang.bokstore.order.BaseIntegrationTest;
import com.javalang.bokstore.order.domain.models.OrderSummary;
import com.javalang.bokstore.order.testdata.TestDataFactory;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-orders.sql")
class OrderControllerTest extends BaseIntegrationTest {

    @Nested
    class CreateOrderTests {

        @Test
        void shouldCreateOrder() {
            mockGetProductByCode("P100", "Product 1", new BigDecimal("25.50"));
            String payload =
                    """
                        {
                             "customer" : {
                                 "name": "Vimal",
                                 "email": "vimal@gmail.com",
                                 "phone": "999999999"
                             },
                             "deliveryAddress" : {
                                 "addressLine1": "Birkelweg",
                                 "addressLine2": "Hans-Edenhofer-Straße 23",
                                 "city": "Berlin",
                                 "state": "Berlin",
                                 "zipCode": "94258",
                                 "country": "Germany"
                             },
                             "items": [
                                 {
                                     "code": "P100",
                                     "name": "Product 1",
                                     "price": 25.50,
                                     "quantity": 1
                                 }
                             ]
                         }
                    """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestForInvalidRequestData() {
            var data = TestDataFactory.createOrderRequestWithInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(data)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class GetOrdersTests {

        @Test
        void shouldReturnAllOrder() {
            List<OrderSummary> orderSummaries = given().when()
                    .get("/api/orders")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(new TypeRef<>() {});
            assertThat(orderSummaries).hasSize(2);
        }
    }

    @Nested
    class GetOrderByOrderNumber {

        @Test
        void shouldReturnOrderByOrderNumber() {
            given().when()
                    .get("/api/orders/{orderNumber}", "order-123")
                    .then()
                    .statusCode(200)
                    .body("orderNumber", is("order-123"))
                    .body("items.size()", is(2));
        }
    }
}
