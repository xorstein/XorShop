package com.xorshop.admin.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;


import com.xorshop.common.entity.Category;
import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.Customer;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
	@Autowired
	private CustomerRepository repo;
	@Autowired
	private TestEntityManager entitymanger;
	
	@Test
	public void Creat() {
		Integer CountryId=63;
		Customer customer=new Customer();
		Country country=entitymanger.find(Country.class, CountryId);
		
		customer.setAddressLine1("LINE1 ");
		customer.setAddressLine2("LINE2 ");
		customer.setCity("CITY");
		customer.setFirstName("FIRSTNAME");
		customer.setLastName("LASTNAME");
		customer.setPhoneNumber("+PHONE");
		customer.setPostalCode("POSTAL");
		customer.setState("STATE");
		customer.setCreatedTime(new Date());
		customer.setEmail("EMAIL");
		customer.setCountry(country);
		customer.setPassword("MYPASSWORD");
		Customer savedCustomer=repo.save(customer);
		
		
	}
	/*@Test
	public void testListAll() {
		Iterable<Customer> customers = repo.findAll();

		customers.forEach(System.out::println);
		
	}
	@Test
	public void updateCustomer() {
		Integer id=5;
		Customer c=repo.findById(id).get();
		c.setAddressLine1("LA PICINE");
		c.setAddressLine2("ZIADIA");
		c.setCity("CONSTANTINE");
		c.setFirstName("NADIR");
		c.setLastName("SAADA");
		c.setPhoneNumber("+58525844");
		c.setPostalCode("25003");
		c.setState("CONSTANTINE");
		c.setCreatedTime(new Date());
		c.setEmail("nadir@gmail.fr");
		c.setPassword("NADIRPASSWORD");
		Customer savedCustomer=repo.save(c);
		
	}
	@Test
	public void deleteCustomer() {
		Integer id=5;
		repo.deleteById(id);
		
	}*/
	/*@Test
	public void VerifiedBYEmailCustomer() {
		String email="mgnfco@live.co.uk";
		Customer c=repo.findByEmail(email);
		System.out.println(c);
		
	}*/
	@Test
	public void VerifiedBYCodeCustomer() {
		
		String code="code123";
		Customer c=repo.findByVerificationCode(code);
		System.out.println(c);
	}
	
	

}
