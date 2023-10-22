package com.xorshop.service;


import java.util.Date;
import java.util.List;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xorshop.common.entity.AuthenticationType;
import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.Customer;
import com.xorshop.common.exception.CustomerNotFoundException;
import com.xorshop.repository.CountryRepository;
import com.xorshop.repository.CustomerRepository;
import com.xorshop.service.impl.ICustomerService;
//import com.xorshop.util.CustomerRegisterUtil;
import com.xorshop.util.CustomerRegisterUtil;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService implements ICustomerService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

	@Autowired 
	private CountryRepository countryRepo;
	
	@Autowired 
	private CustomerRepository customerRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}

	@Override
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}

	@Override
	public void registerCustomer(Customer customer) {
		CustomerRegisterUtil.encodePassword(customer, passwordEncoder);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);
		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);

		customerRepo.save(customer);
		
	}
	
	@Override
	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);

		if (customer == null || customer.isEnabled()) {
			return false;
		} else {
			customerRepo.enable(customer.getId());
			return true;
		}
	}
	
	@Override
	public Customer getCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
	}

	@Override
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		// TODO Auto-generated method stub
		
		LOGGER.info("CustomerService | updateAuthenticationType |  customer : " + customer.toString());
		LOGGER.info("CustomerService | updateAuthenticationType |  type : " + type);
		
		if (customer.getAuthenticationType() == null || !customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
			LOGGER.info("CustomerService | updateAuthenticationType |  AuthenticationType updated");
		}
	}
	
	@Override
	public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode,
			AuthenticationType authenticationType) {
		Customer customer = new Customer();
		customer.setEmail(email);
		setName(name, customer);

		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationType);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setCity("");
		customer.setState("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		customer.setCountry(countryRepo.findByCode(countryCode));

		customerRepo.save(customer);
	}	

	private void setName(String name, Customer customer) {
		String[] nameArray = name.split(" ");
		if (nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		} else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);

			String lastName = name.replaceFirst(firstName + " ", "");
			customer.setLastName(lastName);
		}
	}
	
	public void update(Customer customerInForm) {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();

		if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
			if (!customerInForm.getPassword().isEmpty()) {
				String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
				customerInForm.setPassword(encodedPassword);			
			} else {
				customerInForm.setPassword(customerInDB.getPassword());
			}		
		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}

		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
		customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

		customerRepo.save(customerInForm);
	}

	@Override
	public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		Customer customer = customerRepo.findByEmail(email);
		if (customer != null) {
			String token = RandomString.make(30);
			customer.setResetPasswordToken(token);
			customerRepo.save(customer);

			return token;
		} else {
			throw new CustomerNotFoundException("Impossible de trouver un client avec l'e-mail " + email);
		}
	}

	@Override
	public Customer getByResetPasswordToken(String token) {
		// TODO Auto-generated method stub
		return customerRepo.findByResetPasswordToken(token);
	}

	@Override
	public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
		// TODO Auto-generated method stub
		Customer customer = customerRepo.findByResetPasswordToken(token);
		if (customer == null) {
			throw new CustomerNotFoundException("No customer found: invalid token");
		}

		customer.setPassword(newPassword);
		customer.setResetPasswordToken(null);
		CustomerRegisterUtil.encodePassword(customer, passwordEncoder);

		customerRepo.save(customer);
	}	
}
