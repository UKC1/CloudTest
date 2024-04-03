package com.anyang.convenience.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import com.anyang.convenience.service.ProductService;
import com.anyang.convenience.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReservationService reservationService;

	@MockBean
	private ProductService productService;

	private MockHttpSession session;
	private Customer customer;

	@BeforeEach
	void setUp() {
		session = new MockHttpSession();
		customer = new Customer();
		customer.setCustomerId(1);
		session.setAttribute("customer", customer);
	}

	@Test
	void reservationPage_WhenCustomerIsLoggedIn_ShouldReturnReservationView() throws Exception {
		mockMvc.perform(get("/reserve").session(session))
			.andExpect(status().isOk())
			.andExpect(view().name("reserve"));
	}

	@Test
	void reservationPage_WhenCustomerIsNotLoggedIn_ShouldRedirectToLogin() throws Exception {
		mockMvc.perform(get("/reserve"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"));
	}

	@Test
	void reserveForm_ShouldDisplayProductInfo() throws Exception {
		Integer productId = 1;
		Product product = new Product();
		product.setProductId(productId);
		when(productService.read(productId)).thenReturn(product);

		mockMvc.perform(get("/reserve/{id}", productId).session(session))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("productinfo"))
			.andExpect(view().name("reserve"));

		verify(productService).read(productId);
	}

	@Test
	void showReservations_ShouldDisplayCustomerReservations() throws Exception {
		List<Reservation> reservations = Arrays.asList(new Reservation());
		when(reservationService.getAllReservations(session)).thenReturn(reservations);

		mockMvc.perform(get("/mypage").session(session))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("reservations"))
			.andExpect(view().name("mypage"));

		verify(reservationService).getAllReservations(session);
	}

	@Test
	void rollbackReservation_ShouldCancelReservationAndRedirect() throws Exception {
		Integer reservationId = 1;
		Reservation reservation = new Reservation();
		reservation.setReservationId(reservationId);
		reservation.setQuantity(2);
		reservation.setProduct(new Product());
		when(reservationService.getReservationWithProduct(reservationId)).thenReturn(Optional.of(reservation));

		mockMvc.perform(post("/delete/{id}", reservationId).session(session))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/mypage"));

		verify(productService).rollBack(any(Product.class), eq(2));
		verify(reservationService).delete(reservationId);
	}
}
