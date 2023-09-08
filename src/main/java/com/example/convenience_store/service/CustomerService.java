package com.example.convenience_store.service;

import com.example.convenience_store.model.entity.Customer;
import com.example.convenience_store.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public boolean login(String id, String password, HttpSession session) {
        Optional<Customer> customer = customerRepository.findByIdAndPassword(id, password);

        if(customer.isPresent()) {
            session.setAttribute("customer", customer.get());
            return true;
        }

        return false;
    }

    public void logout(HttpSession session) { session.invalidate();}

    @Cacheable(value = "customer", key = "#id")
    public Customer getUserById(String id){
        return customerRepository.findById(id).orElse(null);
    }
}