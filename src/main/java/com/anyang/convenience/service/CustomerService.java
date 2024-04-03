package com.anyang.convenience.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anyang.convenience.model.entity.Customer;
import com.anyang.convenience.repository.CustomerRepository;

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