package com.koeni.myRetail;

import com.koeni.myRetail.model.CurrentPrice;
import com.koeni.myRetail.model.Product;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClients;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
public class MyRetailController {

    private final MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "products");

    private static final Set<Product> initialProducts = new HashSet<>(){{
        add(new Product(13860428, "The Big Lebowski (Blu-ray)", new CurrentPrice( 13.49, "USD")));
        add(new Product(54456119, "Creamy Peanut Butter 40oz - Good &#38; Gather&#8482;", new CurrentPrice(3.49, "USD")));
        add(new Product(13264003, "Jif Natural Creamy Peanut Butter - 40oz", new CurrentPrice(4.29, "USD")));
        add(new Product(12954218, "Kraft Macaroni &#38; Cheese Dinner Original - 7.25oz", new CurrentPrice(.99, "USD")));
    }};

    MyRetailController() {
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
        return mongoOps.findOne(new Query(where("id").is(id)), Product.class);
    }

    @PutMapping("/myRetail/products/{id}")
    public Product replaceProductPrice(@PathVariable long id, @RequestBody Product product) {
        Query idAndName = new Query(where("id").is(id));
        idAndName.addCriteria(where("name").is(product.getName()));
        if (mongoOps.findOne(idAndName, Product.class) != null) {
            System.out.println("Updating");
            mongoOps.findAndReplace(idAndName, product);
            return product;
        } else {
            return new Product();
        }
    }


}
