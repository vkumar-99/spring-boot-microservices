package com.javalang.bookstore.catalog.web.controllers;

import com.javalang.bookstore.catalog.domain.PagedResult;
import com.javalang.bookstore.catalog.domain.Product;
import com.javalang.bookstore.catalog.domain.ProductNotFoundException;
import com.javalang.bookstore.catalog.domain.ProductService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/products")
class ProductController {

    private final ProductService prodcutService;

    ProductController(ProductService prodcutService) {
        this.prodcutService = prodcutService;
    }

    @GetMapping
    PagedResult<Product> GetProducts(@RequestParam(name = "page", defaultValue = "1") int pageNum) {
        return prodcutService.getProducts(pageNum);
    }

    @GetMapping("/{code}")
    ResponseEntity<Product> GetProductByCode(@PathVariable(value = "code") String code) {
        return prodcutService.getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
