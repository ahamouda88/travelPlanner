package com.travelPlanner.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelPlanner.config.SecurityConfig;
import com.travelPlanner.config.SpringBootConfig;
import com.travelPlanner.persist.entity.User;
import com.travelPlanner.persist.entity.UserRole;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpringBootConfig.class, SecurityConfig.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class AuthenticationTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	protected ObjectMapper mapper = new ObjectMapper();

	private final static String USERS_PATH = "/api/users";

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity())
				.build();

		// Create ADMIN user
		User admin = new User("Ahmed", "Hamouda", "ahamouda", "free", UserRole.ADMIN);
		mockMvc.perform(MockMvcRequestBuilders.post(USERS_PATH, admin).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(admin))).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()));
	}

	@Test
	public void testSuccessfulAuthentication() throws Exception {
		mockMvc.perform(get(USERS_PATH + "/currentuser").with(httpBasic("ahamouda", "free"))).andExpect(status().isOk())
				.andExpect(authenticated().withUsername("ahamouda"));
	}

	@After
	public void testFailedAuthentication() throws Exception {
		mockMvc.perform(get(USERS_PATH + "/currentuser").with(httpBasic("ahamouda", "invalid")))
				.andExpect(status().isUnauthorized());
	}
}
