package com.anyang.convenience_store.repository;

import com.anyang.convenience_store.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByLoginIdAndPasswordHash(String id, String password);

    Optional<Customer> findByLoginId(String id);
}
