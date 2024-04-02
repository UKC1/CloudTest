package com.anyang.convenience_store.service;

import com.anyang.convenience_store.model.entity.Customer;
import com.anyang.convenience_store.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void login() {
        String loginId = "user";
        String password = "password";
        Customer customer = new Customer();
        customer.setLoginId(loginId);
        customer.setPasswordHash(password);

        when(customerRepository.findByLoginIdAndPasswordHash(loginId, password))
                .thenReturn(Optional.of(customer));

        HttpSession session = new MockHttpSession();

        boolean loginResult = customerService.login(loginId, password, session);

        assertTrue(loginResult);
        assertEquals(customer, session.getAttribute("customer"));

        verify(customerRepository).findByLoginIdAndPasswordHash(loginId, password);
    }

    @Test
    void logout() {
        HttpSession session = new MockHttpSession();
        session.setAttribute("customer", new Customer()); // Simulate logged-in user

        customerService.logout(session);
    }
}