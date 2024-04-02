package com.anyang.convenience_store.repository;

import com.anyang.convenience_store.model.entity.Customer;
import com.anyang.convenience_store.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByCustomer(Customer customer);
}

