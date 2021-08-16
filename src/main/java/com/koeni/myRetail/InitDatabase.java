package com.koeni.myRetail;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitDatabase {
    private static final Logger log = LoggerFactory.getLogger(InitDatabase.class);

    @Bean
    CommandLineRunner initProductRepository(ProductRepository productRepository) {
        return args -> {
            log.info("Preloading product: " + productRepository.save(new Product(1, "Test Product A", (float)1.0, "USD")));
            log.info("Preloading product: " + productRepository.save(new Product(2, "Test Product B", (float)2.0, "USD")));
        };
    }
}
