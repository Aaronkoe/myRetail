package com.koeni.myRetail;

import com.koeni.myRetail.model.CurrentPrice;
import com.koeni.myRetail.model.Product;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
@Service
public class MyRetailController {

    private final MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "products");

    private final RestTemplate restTemplate;

    private final String redSkyUrl = "https://redsky.target.com/v3/pdp/tcin/$TCIN$?excludes=taxonomy,price," +
            "promotion,bulk_ship,rating_and_review_reviews," +
            "rating_and_review_statistics,question_answer_statistics&key=candidate";

    private double defaultPrice = 0.00;
    private String defaultCurrencyCode = "USD";

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

    /*
    Allows a user to post a new product. the request body should be json of the product where the id is the
    same id as in the url and the product name is ignored. the initial price and currency code of the product
    will be set to the request body json.

    The mongodb will be updated to reflect the price and currency code.
     */
    @PostMapping("/myRetail/products/{id}")
    public Product postProduct(@PathVariable long id, @RequestBody Product product) {
        if (product.getId() != id) { throw new IllegalArgumentException("Id does not match"); }
        if (mongoOps.findOne(new Query(where("id").is(id)), Product.class) != null) {
            throw new IllegalArgumentException("Product already exists in MongoDB");
        }
        Product newProduct = new Product(id, "", new CurrentPrice(product.getCurrent_price().getValue(), product.getCurrent_price().getCurrency_code()));
        mongoOps.insert(newProduct);
        String name = getNameFromRedSky(id);
        newProduct.setName(name);
        return newProduct;
    }

    /*
    returns all the products as they exist in the mongo db, meaning no names.
     */
    @GetMapping("/myRetail/products_no_names")
    public List<Product> getProductByIdNoNames() {
        return mongoOps.findAll(Product.class);
    }

    /*
    makes many calls to the redsky api to gather all the names of the currently known TCINs and return them
    all as json.
     */
    @GetMapping("/myRetail/products")
    public List<Product> getProductById() {
        List<Product> products = mongoOps.findAll(Product.class);
        for (Product product : products) {
            product.setName(getNameFromRedSky(product.getId()));
        }
        return products;
    }

    /*
    Responds with the product with the given id and its current price/currency code, along with the name that
    comes from the redsky api.
     */
    @GetMapping("/myRetail/products/{id}")
    public Product getProductById(@PathVariable long id) {
        String name = getNameFromRedSky(id);
        Product response = mongoOps.findOne(new Query(where("id").is(id)), Product.class);
        if (response == null) {
            throw new IllegalArgumentException("Id not found in MongoDB");
        }
        response.setName(name);
        return response;
    }

    /*
    Updates the product with the id in the url with the currentPrice attribute of the product
    passed into the request body, as long as the id matches product.getId. ignores the name of the product.
    */
    @PutMapping("/myRetail/products/{id}")
    public Product replaceProductPrice(@PathVariable long id, @RequestBody Product product) {
        if (product.getId() != id) { throw new IllegalArgumentException("Id does not match"); }
        product.setName("");
        Query idQuery = new Query(where("id").is(id));
        if (mongoOps.findOne(idQuery, Product.class) != null) {
            mongoOps.findAndReplace(idQuery, product);
            product.setName(getNameFromRedSky(id));
            return product;
        } else {
            return new Product();
        }
    }

    /*
    Returns the name of a product with the TCIN id by getting the response from redsky then finding the indices
    of the "title" field in the returned json.
     */
    private String getNameFromRedSky(long id) {
        String redSkyResponse;
        try {
            redSkyResponse = restTemplate.getForObject(redSkyUrl.replace("$TCIN$", String.valueOf(id)), String.class);
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("Id not found in redSky api");
        }
        // really naive way of getting the name of the item. This could be improved with a json parser
        // but I just put this together quickly.
        int start_index = redSkyResponse.indexOf("\"title\":\"") + 9;
        int end_index = redSkyResponse.indexOf("\",\"downstream_description\"");
        String name = redSkyResponse.substring(start_index, end_index);
        return name;
    }
}
