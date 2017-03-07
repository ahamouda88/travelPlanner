package com.travelPlanner.controller;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelPlanner.config.SpringBootConfig;
import com.travelPlanner.persist.entity.Trip;
import com.travelPlanner.persist.entity.User;
import com.travelPlanner.persist.entity.UserRole;
import com.travelPlanner.utils.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootConfig.class)
@ActiveProfiles("test")
public class FullControllerTest {

	private final static String USERS_PATH = "/api/users";
	private final static String TRIPS_PATH = "/api/trips";

	@Autowired
	protected WebApplicationContext webApplicationContext;

	protected MockMvc mockMvc;
	protected ObjectMapper mapper;

	private Long userId1;
	private Long userId2;

	@Before
	public void setUp() throws Exception {
		mapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		// Test creating valid users
		User user1 = new User("Ahmed", "Hamouda", "ahamouda", "free", UserRole.ADMIN);
		testCreatedObjectId(USERS_PATH, user1);
		// Test get user, to return the user with the Id
		user1 = getJavaObject(USERS_PATH + "/1", User.class);

		User user2 = new User("Quynh", "To Tuan", "quinni", "Nice", UserRole.ADMIN);
		// Test get user, to return the user with the Id
		testCreatedObjectId(USERS_PATH, user2);
		user2 = getJavaObject(USERS_PATH + "/2", User.class);

		// Test creating invalid user with an existing username
		User user3 = new User("Adam", "Sandler", "ahamouda", "Invalid", UserRole.USER_MANAGER);
		mockMvc.perform(MockMvcRequestBuilders.post(USERS_PATH, user3).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user3))).andExpect(MockMvcResultMatchers.status().isBadRequest());

		userId1 = getJavaObject(USERS_PATH + "/username/ahamouda", User.class).getId();
		userId2 = getJavaObject(USERS_PATH + "/username/quinni", User.class).getId();

		// Creating Trips
		Trip trip1 = new Trip("Barcelona", DateUtils.getDate(2022, 5, 1), DateUtils.getDate(2022, 6, 1));
		trip1.setUser(user1);
		testCreatedObjectId(TRIPS_PATH, trip1);

		Trip trip2 = new Trip("Madrid", DateUtils.getDate(2022, 9, 1), DateUtils.getDate(2022, 9, 11));
		trip2.setUser(user1);
		testCreatedObjectId(TRIPS_PATH, trip2);

		Trip trip3 = new Trip("New York City", DateUtils.getDate(2022, 7, 9), DateUtils.getDate(2022, 7, 20));
		trip3.setUser(user1);
		testCreatedObjectId(TRIPS_PATH, trip3);

		Trip trip4 = new Trip("Chicago", DateUtils.getDate(2022, 11, 10), DateUtils.getDate(2022, 12, 1));
		trip4.setUser(user2);
		testCreatedObjectId(TRIPS_PATH, trip4);

		Trip trip5 = new Trip("Lisbon", DateUtils.getDate(2023, 2, 1), DateUtils.getDate(2023, 6, 1));
		trip5.setUser(user2);
		testCreatedObjectId(TRIPS_PATH, trip5);

		// Test creating invalid Trip
		Trip trip6 = new Trip("Lisbon", DateUtils.getDate(2023, 2, 1), DateUtils.getDate(2023, 1, 1));
		trip6.setUser(user2);
		mockMvc.perform(MockMvcRequestBuilders.post(TRIPS_PATH, trip6).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(trip6))).andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.nullValue()))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void testFunctionality() throws Exception {
		// Test Update User
		User targetUser = new User("Ahmed", "Hamouda", "newUsername", "free", UserRole.ADMIN);
		targetUser.setId(userId1);
		mockMvc.perform(MockMvcRequestBuilders.put(USERS_PATH, targetUser).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(targetUser))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("newUsername")));

		// Test get user by Id
		testReturnedFieldMatch(USERS_PATH + "/" + userId2, "username", "quinni");

		// Test get user by username
		testReturnedFieldMatch(USERS_PATH + "/username/quinni", "lastName", "To Tuan");

		// Test Update Trip
		Trip trip = getJavaObject(TRIPS_PATH + "/2", Trip.class);
		trip.setDestination("Bangkok");

		mockMvc.perform(MockMvcRequestBuilders.put(TRIPS_PATH, trip).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(trip))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.destination", Matchers.is("Bangkok")));

		// Test Remove Trip
		mockMvc.perform(MockMvcRequestBuilders.delete(TRIPS_PATH + "?id=4"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.destination", Matchers.is("Chicago")));

		// Verify size after trip removal
		int expectedSize = 1;
		User user = getJavaObject(USERS_PATH + "/2", User.class);
		Assert.assertEquals(expectedSize, user.getTrips().size());

		// Test Remove Invalid Trip Id
		mockMvc.perform(MockMvcRequestBuilders.delete(TRIPS_PATH + "?id=7"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@After
	public void verifySize() throws Exception {
		// Test get all trips BEFORE removing a user
		testReturnedListSize(TRIPS_PATH, 4);

		// Test get by destination
		testReturnedListSize(TRIPS_PATH + "/search?destination=Barcelona", 1);

		// Test get user trips
		testReturnedListSize(TRIPS_PATH + "/search?userid=1", 3);

		// Test get user with specific destination
		testReturnedListSize(TRIPS_PATH + "/search?destination=New York City&userid=1", 1);

		// Test get user with invalid destination
		testReturnedListSize(TRIPS_PATH + "/search?destination=Cairo&userid=1", 0);

		// Test Get all users
		testReturnedListSize(USERS_PATH, 2);

		// Test remove user
		mockMvc.perform(MockMvcRequestBuilders.delete(USERS_PATH + "?id=" + userId1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("newUsername")));

		// Test remaining number of users
		testReturnedListSize(USERS_PATH, 1);

		// Test get all trips AFTER removing a user
		testReturnedListSize(TRIPS_PATH, 1);
	}

	/*
	 * Generic test methods
	 */
	public <T> void testCreatedObjectId(String path, T object) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post(path, object).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(object))).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()));
	}

	public <T> void testReturnedFieldMatch(String path, String fieldName, String expectedValue) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(path)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$." + fieldName, Matchers.is(expectedValue)));
	}

	public <T> void testReturnedListSize(String path, int size) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(path)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(size)));
	}

	public <T> T getJavaObject(String path, Class<T> classType) throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(path)).andDo(MockMvcResultHandlers.print())
				.andReturn();
		return mapper.readValue(result.getResponse().getContentAsString(), classType);
	}
}
