package com.xorshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.xorshop.common.entity.Customer;
import com.xorshop.repository.CustomerRepository;

public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired 
	private CustomerRepository repo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = repo.findByEmail(email);
		if (customer == null) 
			throw new UsernameNotFoundException("Client introuvable avec l'adresse e-mail: " + email);

		return new CustomerUserDetails(customer);
	}

}
