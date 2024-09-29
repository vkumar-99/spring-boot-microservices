package com.javalang.bookstore.catalog.web.controllers;

import com.javalang.bookstore.catalog.BaseIntegrationTest;
import com.javalang.bookstore.catalog.domain.Product;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Sql("/test-data.sql")
class ProductControllerTest extends BaseIntegrationTest {

    @Test
    void shouldReturnBooks() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("data", hasSize(10))
                .body("totalElements", is(15))
                .body("pageNumber", is(1))
                .body("totalPages", is(2))
                .body("isFirst", is(true))
                .body("isLast", is(false))
                .body("hasNext", is(true))
                .body("hasPrevious", is(false));
    }

    @Test
    void shouldReturnProductByCode() {
        Product product = given().contentType(ContentType.JSON)
                .when()
                .get("/api/products/{code}", "P112")
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .extract()
                .body()
                .as(Product.class);
        assertThat(product.code()).isEqualTo("P112");
        assertThat(product.name()).isEqualTo("The Book Thief");
        assertThat(product.description()).isEqualTo("Nazi Germany. The country is holding its breath. Death has never been busier, and will be busier still.By her brother's graveside, Liesel's life is changed when she picks up a single object, partially hidden in the snow.");
        assertThat(product.price()).isEqualTo(new BigDecimal("30.0"));
    }

    @Test
    void shouldReturn404WhenProductNotExistByCode() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/products/{code}", "P1121")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("type", is("https://api.bookstore.com/errors/not-found"))
                .body("title",is("Product Not Found"));
    }
}