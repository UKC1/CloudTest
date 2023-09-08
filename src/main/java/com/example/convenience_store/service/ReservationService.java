package com.example.convenience_store.service;

import com.example.convenience_store.model.entity.Reservation;
import com.example.convenience_store.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    public Reservation create(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
