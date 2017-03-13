package com.travelPlanner.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
@TestExecutionListeners(listeners = { ServletTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class, WithSecurityContextTestExecutionListener.class })
@ActiveProfiles("test")
public class AppSecurityTest {

	private final static String USERS_PATH = "/api/users";
	private final static String TRIPS_PATH = "/api/trips";

	@Autowired
	protected WebApplicationContext webApplicationContext;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	protected MockMvc mockMvc;
	protected ObjectMapper mapper;

	private User user1;
	private User user2;

	@Before
	public void setUp() throws Exception {
		mapper = new ObjectMapper();
		// The user and roles can be used if we didn't test authentication!!
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).defaultRequest(get("/").with(user("user").roles("ADMIN"))).addFilter(springSecurityFilterChain)
				.build();

		createUsers();
	}

	@Test
//	@WithMockUser(roles = { "ADMIN" })
	public void testUpdateUsers() throws Exception {

		// Test update user with unauthorized user
		User targetUser = new User("Ahmed", "Hamouda", "newUsername", "free", UserRole.ADMIN);
		targetUser.setId(user1.getId());

		mockMvc.perform(put(USERS_PATH, targetUser).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(targetUser))).andExpect(MockMvcResultMatchers.status().isOk());
	}

	/**
	 * Creating a couple of users
	 * 
	 * @throws Exception
	 */
	private void createUsers() throws Exception {
		User tempUser1 = new User("Ahmed", "Hamouda", "ahamouda", "free", UserRole.ADMIN);
		testCreatedObjectId(USERS_PATH, tempUser1);
		user1 = getJavaObject(USERS_PATH + "/1", User.class);

		User tempUser2 = new User("Quynh", "To Tuan", "quinni", "Nice", UserRole.ADMIN);
		testCreatedObjectId(USERS_PATH, tempUser2);
		user2 = getJavaObject(USERS_PATH + "/2", User.class);
	}

	/*
	 * Generic test methods
	 */
	public <T> void testCreatedObjectId(String path, T object) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(path, object).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(object))).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()));
	}

	public <T> T getJavaObject(String path, Class<T> classType) throws Exception {
		MvcResult result = mockMvc.perform(get(path)).andDo(MockMvcResultHandlers.print()).andReturn();
		return mapper.readValue(result.getResponse().getContentAsString(), classType);
	}
}
