package com.javalang.bookstore.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
        properties = {"spring.test.database.replace=none", "spring.datasource.url=jdbc:tc:postgresql:14-alpine:///db"})
@Sql("/test-data.sql")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldReturnProductByCode() {
        ProductEntity entity = productRepository.findByCode("P100").orElseThrow();
        assertThat(entity.getCode()).isEqualTo("P100");
        assertThat(entity.getName()).isEqualTo("The Hunger Games");
        assertThat(entity.getDescription()).isEqualTo("Winning will make you famous. Losing means certain death...");
        assertThat(entity.getPrice()).isEqualTo(new BigDecimal("34.0"));
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotExist() {
        assertThat(productRepository.findByCode("P1001")).isEmpty();
    }
}
