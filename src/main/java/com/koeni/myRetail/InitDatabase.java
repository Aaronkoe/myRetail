package com.koeni.myRetail;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class InitDatabase {
    private static final Logger log = LoggerFactory.getLogger(InitDatabase.class);

    private static final Set<Product> initial_products = new HashSet<>(){{
        add(new Product(13860428, "The Big Lebowski (Blu-ray)", 13.49, "USD"));
        add(new Product(54456119, "Creamy Peanut Butter 40oz - Good &#38; Gather&#8482;", 3.49, "USD"));
        add(new Product(13264003, "Jif Natural Creamy Peanut Butter - 40oz", 4.29, "USD"));
        add(new Product(12954218, "Kraft Macaroni &#38; Cheese Dinner Original - 7.25oz", .99, "USD"));
    }};

    @Bean
    CommandLineRunner initDummyProducts(ProductRepository productRepository) {
        return args -> {
            log.info("Preloading product: " + productRepository.save(new Product(1, "Test Product A", 1.0, "USD")));
            log.info("Preloading product: " + productRepository.save(new Product(2, "Test Product B", 2.0, "EUR")));
        };
    }

    @Bean CommandLineRunner initProductionProducts(ProductRepository productRepository) {
        return args -> {
            for (Product product : initial_products) {
                log.info("Preloading product: " + productRepository.save(product));
            }
        };
    }
}
