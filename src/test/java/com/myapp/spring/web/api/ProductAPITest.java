package com.myapp.spring.web.api;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.spring.model.Product;
import com.myapp.spring.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc

public class ProductAPITest {

	@MockBean
	private ProductRepository repository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@DisplayName("Test Product by ID - GET /api/v1/products/")
	public void testGetProductsById() throws Exception {
		
		//Prepare Mock Product
		Product product =new Product("Oneplus","OnePlus9Pro",60000.00,4.5);
		product.setProductId(1);
		
		// Prepare Mock Service Method
		
		doReturn(Optional.of(product)).when(repository).findById(product.getProductId());
		
		// Perform GET Request
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{id}",1))
		// Validate Status should be 200 ok and json response recived
		
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		//Validate Response body
		
		.andExpect(jsonPath("$.productId",is(1)))
		.andExpect(jsonPath("$.productName",is("Oneplus")))
		.andExpect(jsonPath("$.description",is("OnePlus9Pro")))
		.andExpect(jsonPath("$.price",is(60000.00)))
		.andExpect(jsonPath("$.starRating",is(4.5)));
		
		
	}
	@Test
	@DisplayName("Test All Product - GET /api/v1/products/")
	public void testGetAllProductsById() throws Exception {
		
		//Prepare Mock Product
		Product product1 =new Product("Oneplus","OnePlus9Pro",70000.00,4.5);
		product1.setProductId(25);
		
		Product product2 =new Product("Oneplus","OnePlus8Pro",60000.00,3.5);
		product2.setProductId(26);
		
		List<Product> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		
		// Prepare Mock Service Method
		
		doReturn(products).when(repository).findAll();
		
		// Perform GET Request
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
		// Validate Status should be 200 ok and json response recived
		
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		//Validate Response body
		
		//Object1
		.andExpect(jsonPath("$[0].productId",is(25)))
		.andExpect(jsonPath("$[0].productName",is("Oneplus")))
		.andExpect(jsonPath("$[0].description",is("OnePlus9Pro")))
		.andExpect(jsonPath("$[0].price",is(70000.00)))
		.andExpect(jsonPath("$[0].starRating",is(4.5)))

		//Object2		
		.andExpect(jsonPath("$[1].productId",is(26)))
		.andExpect(jsonPath("$[1].productName",is("Oneplus")))
		.andExpect(jsonPath("$[1].description",is("OnePlus8Pro")))
		.andExpect(jsonPath("$[1].price",is(60000.00)))
		.andExpect(jsonPath("$[1].starRating",is(3.5)));
	
		
	}

	@Test
	@DisplayName("Test Add New Product")
	public void testAddNewProducts() throws Exception {
		
		//Prepare Mock Product
		Product newproduct =new Product("Oneplus","OnePlus9Pro",70000.00,4.5);
		
		Product mockproduct =new Product("Oneplus","OnePlus9Pro",70000.00,4.5);
		mockproduct.setProductId(28);
		
		// Prepare Mock Service Method
		
		doReturn(mockproduct).when(repository).save(ArgumentMatchers.any());
		
		// Perform GET Request
		
		mockMvc.perform(post("/api/v1/products")
		// Validate Status should be 200 ok and json response recived
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(new ObjectMapper().writeValueAsString(newproduct)))
		
		//Validate Response body
		
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(jsonPath("$.productId",is(28)))
		.andExpect(jsonPath("$.productName",is("Oneplus")))
		.andExpect(jsonPath("$.description",is("OnePlus9Pro")))
		.andExpect(jsonPath("$.price",is(70000.00)))
		.andExpect(jsonPath("$.starRating",is(4.5)));
	
		
	}


		
	@Test
	@DisplayName("Test All Product by price - GET /api/v1/products/")
	public void testGetAllProductsByPrice() throws Exception {
		
		//Prepare Mock Product
		Product product1 =new Product("Oneplus","OnePlus9Pro",70000.00,4.5);
		product1.setProductId(1);
		
		Product product2 =new Product("Oneplus","OnePlus8Pro",60000.00,3.5);
		product2.setProductId(2);

		Product product3 =new Product("Oneplus","OnePlus7Pro",40000.00,2.5);
		product3.setProductId(3);
		
		List<Product> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		products.add(product3);
		
		
		double price=50000;
		// Prepare Mock Service Method
		

		doReturn(Optional.of(products)).when(repository)
		.findByPriceGreaterThanEqual(price);
		
		// Perform GET Request
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/findByPrice/{price}",price))
		// Validate Status should be 200 ok and json response recived
		
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		//Validate Response body
		
		//Object1
		.andExpect(jsonPath("$[0].productId",is(1)))
		.andExpect(jsonPath("$[0].productName",is("Oneplus")))
		.andExpect(jsonPath("$[0].description",is("OnePlus9Pro")))
		.andExpect(jsonPath("$[0].price",is(70000.00)))
		.andExpect(jsonPath("$[0].starRating",is(4.5)))

		//Object2		
		.andExpect(jsonPath("$[1].productId",is(2)))
		.andExpect(jsonPath("$[1].productName",is("Oneplus")))
		.andExpect(jsonPath("$[1].description",is("OnePlus8Pro")))
		.andExpect(jsonPath("$[1].price",is(60000.00)))
		.andExpect(jsonPath("$[1].starRating",is(3.5)));
		
		//Object3		
//		.andExpect(jsonPath("$[2].productId",is(29)))
//		.andExpect(jsonPath("$[2].productName",is("Oneplus")))
//		.andExpect(jsonPath("$[2].description",is("OnePlus7Pro")))
//		.andExpect(jsonPath("$[2].price",is(80000.00)))
//		.andExpect(jsonPath("$[2].starRating",is(2.5)));
//	
		
	}

	
	
}
