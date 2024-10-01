package com.javalang.bokstore.order.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import com.javalang.bokstore.order.BaseIntegrationTest;
import com.javalang.bokstore.order.testdata.TestDataFactory;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderControllerTest extends BaseIntegrationTest {

    @Nested
    class CreateOrderTests {

        @Test
        void shouldCreateOrder() {
            mockGetProductByCode("P100", "2Product 1", new BigDecimal("25.50"));
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
                    .post("/api/order")
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
                    .post("/api/order")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
