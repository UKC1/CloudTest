package com.anyang.convenience.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;

import com.anyang.convenience.model.entity.Customer;
import com.anyang.convenience.model.entity.Product;
import com.anyang.convenience.model.entity.Reservation;
import com.anyang.convenience.repository.ReservationRepository;

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
		customer = new Customer();
		product = new Product();

		session.setAttribute("customer", customer);
	}

	@Test
	void save_ShouldCalculateTotalPriceAndSaveReservation() {
		Reservation reservation = new Reservation();
		reservation.setCustomer(customer);
		reservation.setProduct(product);
		reservation.setQuantity(1);
		product.setPrice(1000);
		when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

		Reservation savedReservation = reservationService.save(reservation);

		assertThat(savedReservation.getPrice()).isEqualTo(1000);
		verify(reservationRepository).save(reservation);
	}

	@Test
	void getAllReservations_ShouldReturnReservationsOfLoggedInCustomer() {

		when(reservationRepository.findByCustomer(customer)).thenReturn(
			Arrays.asList(new Reservation(), new Reservation()));

		List<Reservation> reservations = reservationService.getAllReservations(session);

		assertThat(reservations).hasSize(2);
		verify(reservationRepository).findByCustomer(customer);
	}

	@Test
	void getReservationWithProduct_ShouldReturnReservationById() {
		Integer reservationId = 1;
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(new Reservation()));

		Optional<Reservation> reservation = reservationService.getReservationWithProduct(reservationId);

		assertThat(reservation).isPresent();
		verify(reservationRepository).findById(reservationId);
	}

	@Test
	void delete_ShouldDeleteReservationById() {
		Integer reservationId = 1;
		Reservation reservation = new Reservation();
		when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

		reservationService.delete(reservationId);

		verify(reservationRepository).delete(reservation);
	}
}