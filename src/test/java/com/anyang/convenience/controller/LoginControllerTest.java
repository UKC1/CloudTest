package com.anyang.convenience.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.anyang.convenience.model.entity.Customer;
import com.anyang.convenience.service.CustomerService;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerService customerService;

	@Test
	void signupPage_ShouldReturnSignupView() throws Exception {
		mockMvc.perform(get("/signup"))
			.andExpect(status().isOk())
			.andExpect(view().name("signup"));
	}

	@Test
	void loginPage_ShouldReturnLoginView() throws Exception {
		mockMvc.perform(get("/login"))
			.andExpect(status().isOk())
			.andExpect(view().name("login"));
	}

	@Test
	void logout_ShouldInvalidateSessionAndRedirectToLogin() throws Exception {
		mockMvc.perform(get("/logout"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"));
	}

	@Test
	void signupProcess_ShouldCreateCustomerAndRedirectToIndex() throws Exception {
		mockMvc.perform(post("/signup")
				.param("name", "KangMin")
				.param("id", "kangmin123")
				.param("password", "password123"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/index"));

		verify(customerService).save(any(Customer.class));
	}

	@Test
	void loginProcess_WhenAuthenticated_ShouldRedirectToSearch() throws Exception {
		when(customerService.authenticate("kangmin123", "password123"))
			.thenReturn(Optional.of(new Customer()));

		mockMvc.perform(post("/login")
				.param("id", "kangmin123")
				.param("password", "password123"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/search"));

		verify(customerService).authenticate("kangmin123", "password123");
	}

	@Test
	void loginProcess_WhenAuthenticationFails_ShouldReturnToLoginWithErrorMessage() throws Exception {
		when(customerService.authenticate("kangmin123", "wrongpassword"))
			.thenReturn(Optional.empty());

		mockMvc.perform(post("/login")
				.param("id", "kangmin123")
				.param("password", "wrongpassword"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("error"))
			.andExpect(view().name("login"));

		verify(customerService).authenticate("kangmin123", "wrongpassword");
	}
}
