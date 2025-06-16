package com.sls.product_management;

import com.sls.product_management.model.Product;
import com.sls.product_management.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class ProductManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductManagementApplication.class, args);
	}


@Bean
public CommandLineRunner loadData(ProductService productService) {
	return args -> {
		// Create a few products
		Product p1 = productService.addProduct(new Product(null, "Honda", "Honda car sales", new BigDecimal("10000.00"), 50, null));
		Product p2 = productService.addProduct(new Product(null, "Audi", "Audi car sales", new BigDecimal("200000.00"), 10, null));
		Product p3 = productService.addProduct(new Product(null, "Toyota", "Toyota car sales", new BigDecimal("12000.00"), 80, null));

		productService.addSale(p1.getId(), 20);
		productService.addSale(p2.getId(), 2);
		productService.addSale(p3.getId(), 10);
	};
}
}