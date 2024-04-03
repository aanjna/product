package com.tourice.product;

import com.tourice.product.controller.ProductController;
import com.tourice.product.model.Product;
import com.tourice.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SpringJUnitConfig
@WebMvcTest
class ProductApplicationTests {

//	@Test
	void contextLoads() {
	}
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductRepository productRepository;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private ProductController productController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\": \"Test Product\", \"price\": 25.99, \"description\": \"Test Description\"}")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		verify(productRepository, times(1)).save(any());
	}

	@Test
	public void testGetProduct() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setName("Test Product");
		product.setPrice(25.99);
		product.setDescription("Test Description");

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Product"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.price").value(25.99))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"));

		verify(productRepository, times(1)).findById(1L);
		verify(productRepository, times(1)).incrementViewCount(1L);
	}

	@Test
	public void testMostViewedProducts() throws Exception {
		Product product = new Product();
		product.setId(1L);
		product.setName("Test Product");
		product.setPrice(25.99);
		product.setDescription("Test Description");
		product.setViewCount(100);

		when(productRepository.findAll(any())).thenReturn(Collections.singletonList(product));

		mockMvc.perform(MockMvcRequestBuilders.get("/products/most-viewed"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].viewCount").value(100));

		verify(productRepository, times(1)).findAll(any());
	}

	@Test
	public void testDeleteProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/products/1"))
				.andExpect(status().isOk());
		verify(productRepository, times(1)).deleteById(1L);
	}
}
