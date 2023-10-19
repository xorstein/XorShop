package com.xorshop.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.State;
import com.xorshop.common.entity.StateDTO;
import com.xorshop.repository.StateRepository;



@RestController
public class StateRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StateRestController.class);
	
	@Autowired 
	private StateRepository repo;

	@GetMapping("/settings/list_states_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
		
		LOGGER.info("StateRestController | listByCountry is called");
		
		LOGGER.info("StateRestController | listByCountry | countryId : " + countryId);
		
		List<State> listStates = repo.findByCountryOrderByNameAsc(new Country(countryId));
		
		LOGGER.info("StateRestController | listByCountry | listStates.size() : " + listStates.size());
		
		List<StateDTO> result = new ArrayList<>();

		for (State state : listStates) {
			result.add(new StateDTO(state.getId(), state.getName()));
		}
		
		LOGGER.info("StateRestController | listByCountry | result : " + result.toString());

		return result;
	}

	


}
