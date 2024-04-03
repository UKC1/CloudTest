package com.anyang.convenience_store.service;

import com.anyang.convenience_store.model.entity.Customer;
import com.anyang.convenience_store.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldSaveCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setName("Test User");
        customer.setLoginId("testuser");
        customer.setPasswordHash("password123");

        // When
        customerService.save(customer);

        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void authenticate_ShouldReturnCustomer_WhenCredentialsMatch() {
        // Given
        String id = "testuser";
        String password = "password123";
        Customer customer = new Customer();
        customer.setLoginId(id);
        customer.setPasswordHash(password);
        when(customerRepository.findByLoginIdAndPasswordHash(id, password)).thenReturn(Optional.of(customer));

        // When
        Optional<Customer> result = customerService.authenticate(id, password);

        // Then
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getLoginId());
    }

    @Test
    void authenticate_ShouldReturnEmptyOptional_WhenCredentialsDoNotMatch() {
        // Given
        String id = "testuser";
        String password = "wrongpassword";
        when(customerRepository.findByLoginIdAndPasswordHash(id, password)).thenReturn(Optional.empty());

        // When
        Optional<Customer> result = customerService.authenticate(id, password);

        // Then
        assertFalse(result.isPresent());
    }
}
