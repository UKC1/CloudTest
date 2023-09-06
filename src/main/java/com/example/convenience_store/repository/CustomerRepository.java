package com.example.convenience_store.repository;

import com.example.convenience_store.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByIdAndPassword(String id, String password);

    Optional<Customer> findById(String id);
}
