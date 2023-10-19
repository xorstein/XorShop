package com.xorshop.admin.state;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xorshop.admin.country.CountryRepository;

import com.xorshop.admin.country.state.StateRepository;
import com.xorshop.admin.country.state.StateRestController;
import com.xorshop.common.entity.Country;
import com.xorshop.common.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {
	
	MockMvc mockMvc;

	
	ObjectMapper objectMapper;

	
	CountryRepository countryRepo;

	
	StateRepository stateRepo;
	
	@Autowired
	private StateRestController myController;
	
	
	@Autowired
	public StateRestControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, StateRepository stateRepo,CountryRepository countryRepo) {
		super();
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
		this.stateRepo = stateRepo;
		this.countryRepo = countryRepo;
	}
	 @BeforeEach
	 public void setUp() {
		  this.mockMvc = MockMvcBuilders.standaloneSetup( myController).build();
	 }
	/* @Test
		@WithMockUser(username = "mgnfco@live.com", password = "4055582h", roles = "Admin")
		public void testListByCountries() throws Exception {
			Integer countryId = 4;
			String url = "/states/list_by_country/" + countryId;

			MvcResult result = mockMvc.perform(get(url))
					.andExpect(status().isOk())
					.andDo(print())
					.andReturn();

			String jsonResponse = result.getResponse().getContentAsString();
			State[] states = objectMapper.readValue(jsonResponse, State[].class);
                for(State s :states) {
                	System.out.println(s.getName());;
                }
			//assertThat(states).hasSizeGreaterThan(1);
		}*/

		/*@Test
		@WithMockUser(username = "mgnfco@live.com", password = "4055582h", roles = "Admin")
		public void testCreateState() throws Exception {
			String url = "/states/save";
			Integer countryId = 4;
			Country country = countryRepo.findById(countryId).get();
			State state = new State("Arizona", country);

			MvcResult result = mockMvc.perform(post(url).contentType("application/json")
					.content(objectMapper.writeValueAsString(state))
					.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

			String response = result.getResponse().getContentAsString();
			Integer stateId = Integer.parseInt(response);
			Optional<State> findById = stateRepo.findById(stateId);

			//assertThat(findById.isPresent());		
		}*/

		/*@Test
		@WithMockUser(username = "mgnfco@live.com", password = "4055582h", roles = "Admin")
		public void testUpdateState() throws Exception {
			String url = "/states/save";
			Integer stateId = 2;
			String stateName = "9ssantina";

			State state = stateRepo.findById(stateId).get();
			state.setName(stateName);

			mockMvc.perform(post(url).contentType("application/json")
				.content(objectMapper.writeValueAsString(state))
				.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(stateId)));

			Optional<State> findById = stateRepo.findById(stateId);
			//assertThat(findById.isPresent());

			State updatedState = findById.get();
			//assertThat(updatedState.getName()).isEqualTo(stateName);

		}*/

		@Test
		@WithMockUser(username = "mgnfco@live.com", password = "4055582h", roles = "Admin")
		public void testDeleteState() throws Exception {
			Integer stateId = 6;
			String uri = "/states/delete/" + stateId;

			mockMvc.perform(get(uri)).andExpect(status().isOk());

			Optional<State> findById = stateRepo.findById(stateId);

			//assertThat(findById).isNotPresent();
		}
	 
	 

}
