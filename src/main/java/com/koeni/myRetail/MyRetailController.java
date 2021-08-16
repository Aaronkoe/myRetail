package com.koeni.myRetail;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRetailController {

    private final ProductRepository productRepository;

    MyRetailController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/ping")
    public static String getPing() {
        return "Pong";
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product with ID not found"));
    }

}
