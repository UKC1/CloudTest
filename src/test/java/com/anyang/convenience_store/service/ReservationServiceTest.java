package com.anyang.convenience_store.service;

import com.anyang.convenience_store.model.entity.Customer;
import com.anyang.convenience_store.model.entity.Product;
import com.anyang.convenience_store.model.entity.Reservation;
import com.anyang.convenience_store.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private MockHttpSession session;
    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        customer = new Customer(); // Initialize Customer
        product = new Product(); // Initialize Product

        // Customer and Product initialization logic here
        session.setAttribute("customer", customer);
    }

    @Test
    void save() {
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setProduct(product);
        reservation.setQuantity(1);
        product.setPrice(1000); // Example price
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // When
        Reservation savedReservation = reservationService.save(reservation);

        // Then
        assertThat(savedReservation.getPrice()).isEqualTo(1000);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void getAllReservations() {
        // Given
        when(reservationRepository.findByCustomer(customer)).thenReturn(Arrays.asList(new Reservation(), new Reservation()));

        // When
        List<Reservation> reservations = reservationService.getAllReservations(session);

        // Then
        assertThat(reservations).hasSize(2);
        verify(reservationRepository).findByCustomer(customer);
    }

    @Test
    void getReservationWithProduct() {
        // Given
        Integer reservationId = 1;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(new Reservation()));

        // When
        Optional<Reservation> reservation = reservationService.getReservationWithProduct(reservationId);

        // Then
        assertThat(reservation).isPresent();
        verify(reservationRepository).findById(reservationId);
    }

    @Test
    void delete() {
        // Given
        Integer reservationId = 1;
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // When
        reservationService.delete(reservationId);

        // Then
        verify(reservationRepository).delete(reservation);
    }
}