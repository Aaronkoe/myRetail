# myRetail
A demo RESTful API dubbed myRetail for the Target Tech interview process

# Problem statement

see supplied pdf for problem statement

# Solution description

This solution implements the desired REST api using Spring Boot with MongoDB. There is a POJO to represent both a Product and a CurrentPrice located in the `model` directory. MongoDB is used to store a Product (which contains a CurrentPrice), and the id of a price is primarily used to lookup the value and currency code of a product. The database doesn't store the name of any products, though the name field is mapped to an empty string for each record. The api retrieves names as needed from the RedSky api provided.

The application exposes 5 end points:

```
GET -> <ip_address>:8080/products/{id}
```

Will retrieve a JSON object with the name, id, value, and currency code of a product. If the id has not been seen yet, the behavior of the API is undefined. (This is somewhere the application could be extended)

```
GET -> <ip_address>:8080/products_no_names
```

Will retrieve all the products stored in the database, returning them as is. This means all product will have empty strings as their names, but the id/value/currency code will be up to date.

```
GET -> <ip_address>:8080/products
```

Will retrieve all the products, gathering their names from the RedSky api. This operation can be expensive due to the latency in communicating to the RedSky api.

```
PUT -> <ip_address>:8080/products/{id}


req body:
{
  "id":{id},
  "name":<ignored>,
  "current_price":
  {
    "value":{value},
    "currency_code":{currency_code}
  }
}
```

Will set the value and currency code of the product with {id} to {value} and {currency_code}. If the JSON body {id} doesn't match the {id} in the URL, the behavior is undefined. If the {id} has not been seen yet, the behavior is undefined.

```
POST -> <ip_address>:8080/products/{id}

req body: same as above
```

Will create a new product with {id} and initialize its value and currency code to {value} and {currency_code}. If the {id} of the request body and URL don't match, the behavior is undefined. If the {id} is not a valid RedSky id, the behavior is undefined.

There are a few tests defined for the api above which can be viewed in the `test` directory, and are run automatically when building to a jar.

# Using the API

The api should be accessible at `koenigsberg.me:8080/myRetail/`, where I have an instance of the api running. However, my ip address is dynamic from my ISP, so it may change, at which point the `koenigsberg.me` domain name points to the wrong ip address. Use `koenigsberg.me:8080/myRetail/ping` to make sure the server is reachable. It should respond with the string `Pong` if it is.

Otherwise, the project can be imported compiled to a jar with `mvnw target` on your local machine, in which case requests should go to `localhost:8080/...`.
