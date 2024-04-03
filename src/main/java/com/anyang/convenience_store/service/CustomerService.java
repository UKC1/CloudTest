package com.anyang.convenience_store.service;

import com.anyang.convenience_store.model.entity.Customer;
import com.anyang.convenience_store.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public Optional<Customer> authenticate(String id, String password) {
        return customerRepository.findByLoginIdAndPasswordHash(id, password);
    }
}