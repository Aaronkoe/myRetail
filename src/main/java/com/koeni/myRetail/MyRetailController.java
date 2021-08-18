package com.koeni.myRetail;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Map<String, Object> getProductById(@PathVariable long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product with ID not found"));
        return product.toRequestStructure();
    }

    @PutMapping("/products/{id}")
    public Map<String, Object> replaceProductPrice(@PathVariable long id, @RequestBody Map<String, Object> body) {
        Product product = Product.fromRequestStructure(body);
        productRepository.save(product);
        return product.toRequestStructure();
    }


}
