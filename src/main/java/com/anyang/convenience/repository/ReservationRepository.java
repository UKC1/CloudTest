package com.anyang.convenience.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anyang.convenience.model.entity.Customer;
import com.anyang.convenience.model.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
	List<Reservation> findByCustomer(Customer customer);
}

