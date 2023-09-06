package com.example.convenience_store.service;

import com.example.convenience_store.model.entity.Customer;
import com.example.convenience_store.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Cacheable(value = "customer", key = "#id")
    public Customer getUserById(String id){
        return customerRepository.findById(id).orElse(null);
    }
}
