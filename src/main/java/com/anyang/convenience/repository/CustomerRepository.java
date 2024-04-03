package com.anyang.convenience.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anyang.convenience.model.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	Optional<Customer> findByLoginIdAndPasswordHash(String id, String password);

	Optional<Customer> findByLoginId(String id);
}
