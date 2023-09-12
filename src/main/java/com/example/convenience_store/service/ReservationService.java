package com.example.convenience_store.service;

import com.example.convenience_store.model.entity.Customer;
import com.example.convenience_store.model.entity.Reservation;
import com.example.convenience_store.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation save(Reservation reservation) {
        // 가격을 계산하여 설정
        int totalPrice = reservation.getProduct().getPrice() * reservation.getQuantity();
        reservation.setPrice(totalPrice);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations(HttpSession session) {
        Customer sessionCustomer = (Customer) session.getAttribute("customer");
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
