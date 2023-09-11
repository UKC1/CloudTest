package com.example.convenience_store.service;

import com.example.convenience_store.model.entity.Reservation;
import com.example.convenience_store.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation save(Reservation reservation) {
        int totalPrice = reservation.getProduct().getPrice() * reservation.getQuantity();
        reservation.setPrice(totalPrice);
        return reservationRepository.save(reservation);
    }
}
