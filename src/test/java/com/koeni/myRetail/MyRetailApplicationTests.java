package com.koeni.myRetail;

import com.koeni.myRetail.model.CurrentPrice;
import com.koeni.myRetail.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyRetailApplicationTests {

	@Autowired
	MyRetailController controller;

	@Autowired
	private TestRestTemplate restTemplate;

	private long bigLebowskiId = 13860428;
	private String bigLebowskiName = "The Big Lebowski (Blu-ray)";
	private double zeroTolerance = .0001;

	/*
	Assert that a GET responds with a product with the correct name and id.
	 */
	@Test
	void getProductValidIdNameMatches() {
		Product result = this.restTemplate.getForObject("/myRetail/products/" + bigLebowskiId, Product.class);
		assertThat(result.getName()).isEqualTo(bigLebowskiName);
		assertThat(result.getCurrent_price()).isNotEqualTo(null);
	}

	/*
	Assert that a put to update the price and currency code of an object does so.
	 */
	@Test
	void putProductValidIdPriceAndCurrencyCode() {
		Product result = this.restTemplate.getForObject("/myRetail/products/" + bigLebowskiId, Product.class);
		double currentValue = result.getCurrent_price().getValue();
		double newValue = currentValue + 10;
		String currentCurrencyCode = result.getCurrent_price().getCurrency_code();
		// If currency code is USD, change it to EUR, otherwise change it to USD
		String newCurrencyCode = currentCurrencyCode.equals("USD") ? "EUR" : "USD";
		result.getCurrent_price().setValue(newValue);
		result.getCurrent_price().setCurrency_code(newCurrencyCode);
		this.restTemplate.put("/myRetail/products/"+bigLebowskiId, result, Product.class);
		Product secondResult = this.restTemplate.getForObject("/myRetail/products/" + bigLebowskiId, Product.class);
		assertThat(newValue - secondResult.getCurrent_price().getValue()).isLessThan(zeroTolerance);
		assertThat(secondResult.getCurrent_price().getCurrency_code()).isEqualTo(newCurrencyCode);
		assertThat(result.getName()).isEqualTo(secondResult.getName());
		assertThat(result.getId()).isEqualTo(secondResult.getId());
	}

	/*
	Assert that attempting to update the price of a mismatched id fails.
	 */
	@Test
	void putProductIdMismatch() {
		Product wrongId = new Product(1, "", new CurrentPrice(0.0, "USD"));
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			controller.replaceProductPrice(bigLebowskiId, wrongId);
		});
		String expectedMessage = "Id does not match";
		String actualMessage = exception.getMessage();
		assertThat(expectedMessage).isEqualTo(actualMessage);
	}

}
