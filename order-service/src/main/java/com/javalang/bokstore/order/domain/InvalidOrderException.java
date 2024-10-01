package com.javalang.bokstore.order.domain;

public class InvalidOrderException extends RuntimeException {

    public InvalidOrderException() {}

    public InvalidOrderException(String message) {
        super(message);
    }

    public static InvalidOrderException forInvalidProductCode(String code) {
        return new InvalidOrderException("Invalid product code: " + code);
    }

    public static InvalidOrderException forProductPriceMismatch(String code) {
        return new InvalidOrderException("Product price is not matching");
    }
}
