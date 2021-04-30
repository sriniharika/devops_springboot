package com.myapp.spring.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.spring.model.Product;
import com.myapp.spring.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc

public class ProductIntegrationTest {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private MockMvc mockMvc;
	
	private static File DATA_JSON = Paths.get("src","test","resources","products.json").toFile();
	
	@BeforeEach
	public void setUp() throws JsonEOFException,JsonMappingException,IOException{
		
	Product products[]= new ObjectMapper().readValue(DATA_JSON, Product[].class);
	
	Arrays.stream(products).forEach(repository::save);
	
	}
	
	@AfterEach
	public void cleanUp() {
		repository.deleteAll();
	}
	
	
	
	
	@Test
	@DisplayName("Test Product by ID - GET /api/v1/products/")
	public void testGetProductsById() throws Exception {
		
		
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
		
		
		// Perform GET Request
		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
		// Validate Status should be 200 ok and json response recived
		
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		
		//Validate Response body
		
		//Object1
		.andExpect(jsonPath("$[0].productId",is(1)))
		.andExpect(jsonPath("$[0].productName",is("Oneplus")))
		.andExpect(jsonPath("$[0].description",is("OnePlus9Pro")))
		.andExpect(jsonPath("$[0].price",is(60000.00)))
		.andExpect(jsonPath("$[0].starRating",is(4.5)))

		//Object2		
		.andExpect(jsonPath("$[1].productId",is(2)))
		.andExpect(jsonPath("$[1].productName",is("Sasmung")))
		.andExpect(jsonPath("$[1].description",is("GalaxyNote12")))
		.andExpect(jsonPath("$[1].price",is(50000.00)))
		.andExpect(jsonPath("$[1].starRating",is(4.1)));
	
		
	}

	//@Test
	@DisplayName("Test Add New Product")
	public void testAddNewProducts() throws Exception {
		
		//Prepare Mock Product
		Product newproduct =new Product("Redmi","Note9",20000.00,1.5);
		
		// Perform GET Request
		
		mockMvc.perform(post("/api/v1/products")
		// Validate Status should be 200 ok and json response recived
		.contentType(MediaType.APPLICATION_JSON_VALUE)
		.content(new ObjectMapper().writeValueAsString(newproduct)))
		
		//Validate Response body
		
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(jsonPath("$.productId",is(4)))
		.andExpect(jsonPath("$.productName",is("Redmi")))
		.andExpect(jsonPath("$.description",is("Note9")))
		.andExpect(jsonPath("$.price",is(20000.00)))
		.andExpect(jsonPath("$.starRating",is(1.5)));
	
		
	}


		
	@Test
	@DisplayName("Test All Product by price - GET /api/v1/products/")
	public void testGetAllProductsByPrice() throws Exception {
		
		double price=50000;
		// Prepare Mock Service Method
		
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
		.andExpect(jsonPath("$[0].price",is(60000.00)))
		.andExpect(jsonPath("$[0].starRating",is(4.5)))

		//Object2		
		.andExpect(jsonPath("$[1].productId",is(2)))
		.andExpect(jsonPath("$[1].productName",is("Sasmung")))
		.andExpect(jsonPath("$[1].description",is("GalaxyNote12")))
		.andExpect(jsonPath("$[1].price",is(50000.00)))
		.andExpect(jsonPath("$[1].starRating",is(4.1)));
		
		//Object3		
//		.andExpect(jsonPath("$[2].productId",is(29)))
//		.andExpect(jsonPath("$[2].productName",is("Oneplus")))
//		.andExpect(jsonPath("$[2].description",is("OnePlus7Pro")))
//		.andExpect(jsonPath("$[2].price",is(80000.00)))
//		.andExpect(jsonPath("$[2].starRating",is(2.5)));
//	
		
	}

	
	
}
