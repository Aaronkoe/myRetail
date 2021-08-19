package com.koeni.myRetail;

import com.koeni.myRetail.model.CurrentPrice;
import com.koeni.myRetail.model.Product;
import com.mongodb.client.MongoClients;
import org.bson.json.JsonObject;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
@Service
public class MyRetailController {

    private final MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "products");

    private final RestTemplate restTemplate;

    private final String redSkyUrl = "https://redsky.target.com/v3/pdp/tcin/$TCIN$?excludes=taxonomy,price," +
            "promotion,bulk_ship,rating_and_review_reviews," +
            "rating_and_review_statistics,question_answer_statistics&key=candidate";

    private static final Set<Product> initialProducts = new HashSet<>(){{
        add(new Product(13860428, "", new CurrentPrice( 13.49, "USD")));
        add(new Product(54456119, "", new CurrentPrice(3.49, "USD")));
        add(new Product(13264003, "", new CurrentPrice(4.29, "USD")));
        add(new Product(12954218, "", new CurrentPrice(.99, "USD")));
    }};

    MyRetailController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        for (Product product : initialProducts) {
            mongoOps.remove(new Query(where("id").is(product.getId())), Product.class);
            mongoOps.insert(product);
        }
    }

    @GetMapping("/myRetail/ping")
    public static String getPing() {
        return "Pong";
    }

    @GetMapping("/myRetail/products/{id}")
    public Product getProductById(@PathVariable long id) {
        String redSkyResponse = restTemplate.getForObject(redSkyUrl.replace("$TCIN$", String.valueOf(id)), String.class);
        System.out.println(redSkyResponse);
        int start_index = redSkyResponse.indexOf("\"title\":\"") + 9;
        int end_index = redSkyResponse.indexOf("\",\"downstream_description\"");
        String name = redSkyResponse.substring(start_index, end_index);
        return mongoOps.findOne(new Query(where("id").is(id)), Product.class);
    }

    @PutMapping("/myRetail/products/{id}")
    public Product replaceProductPrice(@PathVariable long id, @RequestBody Product product) {
        Query idAndName = new Query(where("id").is(id));
        idAndName.addCriteria(where("name").is(product.getName()));
        if (mongoOps.findOne(idAndName, Product.class) != null) {
            mongoOps.findAndReplace(idAndName, product);
            return product;
        } else {
            return new Product();
        }
    }


}
