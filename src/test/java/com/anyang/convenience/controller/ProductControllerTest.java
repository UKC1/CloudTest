package com.anyang.convenience.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.anyang.convenience.model.entity.Customer;
import com.anyang.convenience.model.entity.Product;
import com.anyang.convenience.model.entity.Reservation;
import com.anyang.convenience.model.entity.Store;
import com.anyang.convenience.service.ProductService;
import com.anyang.convenience.service.ReservationService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@MockBean
	private ReservationService reservationService;

	private MockHttpSession session;
	private Customer customer;
	private Store store;

	@BeforeEach
	void setUp() {
		session = new MockHttpSession();
		customer = new Customer();
		customer.setCustomerId(1);
		session.setAttribute("customer", customer);
		store = new Store();
		store.setStoreId(1);
		store.setName("CU");
	}

	@Test
	void showSearchPage_WhenCustomerIsLoggedIn_ShouldReturnSearchView() throws Exception {
		mockMvc.perform(get("/search").session(session))
			.andExpect(status().isOk())
			.andExpect(view().name("search"));
	}

	@Test
	void showSearchPage_WhenCustomerIsNotLoggedIn_ShouldRedirectToLogin() throws Exception {
		mockMvc.perform(get("/search"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"));
	}

	@Test
	void searchItem_ShouldAddFoundProductsToModel() throws Exception {
		String productName = "우유";
		List<Product> foundProducts = Arrays.asList(new Product());
		when(productService.findProductByName(productName)).thenReturn(foundProducts);

		mockMvc.perform(post("/search").param("name", productName))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("foundProducts"))
			.andExpect(view().name("search"));

		verify(productService).findProductByName(productName);
	}

	@Test
	void searchItem_WhenNoProductsFound_ShouldAddMessageToModel() throws Exception {
		String productName = "NotExist";
		when(productService.findProductByName(productName)).thenReturn(Arrays.asList());

		mockMvc.perform(post("/search").param("name", productName))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("message"))
			.andExpect(view().name("search"));

		verify(productService).findProductByName(productName);
	}

	@Test
	void productUpdateForm_ShouldCreateReservationAndRedirect() throws Exception {
		Product product = new Product();
		product.setProductId(1);
		product.setPrice(100);
		product.setQuantity(2);
		product.setStore(store);

		session.setAttribute("product", product);

		when(productService.update(any(Product.class))).thenReturn(product);

		mockMvc.perform(post("/update").session(session).flashAttr("productRequest", product))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/mypage"));

		verify(productService).update(any(Product.class));
		verify(reservationService).save(any(Reservation.class));
	}
}
