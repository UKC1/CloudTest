package com.anyang.convenience.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anyang.convenience.model.entity.Customer;
import com.anyang.convenience.model.entity.Reservation;
import com.anyang.convenience.repository.ReservationRepository;

@Service
public class ReservationService {
	@Autowired
	private ReservationRepository reservationRepository;

	public Reservation save(Reservation reservation) {
		int totalPrice = reservation.getProduct().getPrice() * reservation.getQuantity();
		reservation.setPrice(totalPrice);

		return reservationRepository.save(reservation);
	}

	public List<Reservation> getAllReservations(HttpSession session) {
		Customer sessionCustomer = (Customer)session.getAttribute("customer");
		return reservationRepository.findByCustomer(sessionCustomer);
	}

	public Optional<Reservation> getReservationWithProduct(Integer id) {
		return reservationRepository.findById(id);
	}

	public void delete(Integer id) {
		Reservation existingReservation = reservationRepository.findById(id).orElse(null);
		if (existingReservation != null) {
			reservationRepository.delete(existingReservation);
		}
	}

}
