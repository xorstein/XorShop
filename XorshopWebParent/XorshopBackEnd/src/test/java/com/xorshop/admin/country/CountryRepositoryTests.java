package com.xorshop.admin.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import com.xorshop.common.entity.Country;
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {
	@Autowired 
	private CountryRepository repo;

	/*@Test
	public void testCreateCountry() {
		Country country = repo.save(new Country("Algeria", "DZ"));
		//Country country1 = repo.save(new Country("France", "FR"));
		//Country country2 = repo.save(new Country("united states of america", "USA"));
		////assertThat(country).isNotNull();
		//assertThat(country.getId()).isGreaterThan(0);
	}*/

	@Test
	public void testListCountries() {
		List<Country> listCountries = repo.findAllByOrderByNameAsc();
		listCountries.forEach(System.out::println);

		assertThat(listCountries.size()).isGreaterThan(0);
	}

	/*@Test
	public void testUpdateCountry() {
		Integer id = 1;
		String name = "Republic of Algeria";

		Country country = repo.findById(id).get();
		country.setName(name);

		Country updatedCountry = repo.save(country);

		//assertThat(updatedCountry.getName()).isEqualTo(name);
	}*/

	/*@Test
	public void testGetCountry() {
		Integer id = 1;		
		Country country = repo.findById(id).get();
		System.out.println("Code "+country.getCode()+" name: "+ country.getName()+" .");
	}*/

	/*@Test
	public void testDeleteCountry() {
		Integer id = 1;
		repo.deleteById(id);

		//Optional<Country> findById = repo.findById(id);
		//assertThat(findById.isEmpty());
	}*/
}