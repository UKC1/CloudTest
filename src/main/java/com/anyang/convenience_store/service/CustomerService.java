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

    public boolean login(String id, String password, HttpSession session) {
        Optional<Customer> customer = customerRepository.findByLoginIdAndPasswordHash(id, password);

        if(customer.isPresent()) {
            session.setAttribute("customer", customer.get());
            return true;
        }

        return false;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}