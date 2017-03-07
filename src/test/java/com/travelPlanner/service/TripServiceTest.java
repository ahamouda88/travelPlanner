package com.travelPlanner.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.travelPlanner.config.SpringBootConfig;
import com.travelPlanner.persist.dao.TripDao;
import com.travelPlanner.persist.dao.UserDao;
import com.travelPlanner.persist.entity.Trip;
import com.travelPlanner.persist.entity.User;
import com.travelPlanner.persist.entity.UserRole;
import com.travelPlanner.service.TripService;
import com.travelPlanner.service.UserService;
import com.travelPlanner.service.impl.TripServiceImpl;
import com.travelPlanner.service.impl.UserServiceImpl;
import com.travelPlanner.utils.DateUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = { SpringBootConfig.class, TripServiceImpl.class, TripDao.class, UserServiceImpl.class,
		UserDao.class })
@EntityScan(basePackages = "com.travelPlanner.persist.entity")
public class TripServiceTest {

	@Autowired
	private TripService<Long> tripService;

	@Autowired
	private UserService<Long> userService;

	@Before
	public void testCreateTrip() {
		// Create users
		User user1 = new User("John", "Snow", "admin", "admin", UserRole.ADMIN);
		userService.createUser(user1);

		User user2 = new User("Andres", "Iniesta", "barca", "barcelona", UserRole.USER_MANAGER);
		userService.createUser(user2);

		User user3 = new User("Lionel", "Messi", "leo", "thiago", UserRole.REGULAR_USER);
		userService.createUser(user3);

		// Create trips
		// Trips associated with user1
		Trip trip1 = new Trip("London", DateUtils.getDate(2022, 5, 1), DateUtils.getDate(2022, 6, 1), "UK");
		trip1.setUser(user1);
		boolean expectedCheck = true;
		boolean actualCheck = tripService.createTrip(trip1);
		Assert.assertEquals(expectedCheck, actualCheck);

		Trip trip2 = new Trip("Alexandria", DateUtils.getDate(2022, 3, 1), DateUtils.getDate(2022, 3, 15), "Egypt");
		trip2.setUser(user1);
		actualCheck = tripService.createTrip(trip2);
		Assert.assertEquals(expectedCheck, actualCheck);

		// Trips associated with user2
		Trip trip3 = new Trip("Barcelona", DateUtils.getDate(2022, 8, 1), DateUtils.getDate(2022, 8, 10), "Spain");
		trip3.setUser(user2);
		actualCheck = tripService.createTrip(trip3);
		Assert.assertEquals(expectedCheck, actualCheck);

		Trip trip4 = new Trip("Alexandria", DateUtils.getDate(2022, 10, 1), DateUtils.getDate(2022, 10, 23), "Egypt");
		trip4.setUser(user2);
		actualCheck = tripService.createTrip(trip4);
		Assert.assertEquals(expectedCheck, actualCheck);

		// Trips associated with user3
		Trip trip5 = new Trip("Paris", DateUtils.getDate(2022, 7, 1), DateUtils.getDate(2022, 8, 1), "France");
		trip5.setUser(user3);
		actualCheck = tripService.createTrip(trip5);
		Assert.assertEquals(expectedCheck, actualCheck);
	}

	@Test
	public void testCreateTripWithInvalidDates() {
		User user = new User("Mickel", "Jordan", "newUsername", "newPassword", UserRole.ADMIN);
		userService.createUser(user);

		Trip trip = new Trip("Paris", DateUtils.getDate(2017, 1, 1), DateUtils.getDate(2017, 8, 1), "France");
		trip.setUser(user);
		boolean expectedCheck = false;
		boolean actualCheck = tripService.createTrip(trip);
		Assert.assertEquals(expectedCheck, actualCheck);
	}

	@Test
	public void testGetAllTrips() {
		List<Trip> tripList = tripService.getAllTrips();
		int expectedTrips = 5;
		int actualTrips = tripList.size();
		Assert.assertEquals(expectedTrips, actualTrips);
	}

	@Test
	public void testGetByDestination() {
		List<Trip> tripList = tripService.getFilteredTrips("Alexandria", null, null, null);
		int expectedTrips = 2;
		int actualTrips = tripList.size();
		Assert.assertEquals(expectedTrips, actualTrips);
	}

	@Test
	public void testUpdateTrip() {
		Trip trip = tripService.getAllTrips().get(2);
		Trip updatedTrip = Trip.copyTrip(trip);
		updatedTrip.setComment("New Comment");

		boolean expectedCheck = true;
		boolean actualCheck = tripService.updateTrip(updatedTrip);
		Assert.assertEquals(expectedCheck, actualCheck);
	}

	@Test
	public void testUpdateTripWithNullDate() {
		Trip trip = tripService.getAllTrips().get(2);
		Trip updatedTrip = Trip.copyTrip(trip);
		updatedTrip.setEndDate(null);

		boolean expectedCheck = false;
		boolean actualCheck = tripService.updateTrip(updatedTrip);
		Assert.assertEquals(expectedCheck, actualCheck);
	}

	@Test
	public void testDeleteTrip() {
		Long tripId = tripService.getAllTrips().get(2).getId();

		Trip removedTrip = tripService.deleteTrip(tripId);
		Assert.assertNotNull(removedTrip);

		List<Trip> tripList = tripService.getAllTrips();
		int expectedTrips = 4;
		int actualTrips = tripList.size();
		Assert.assertEquals(expectedTrips, actualTrips);

		// Test invalid delete
		Trip invalidTrip = tripService.deleteTrip(null);
		Assert.assertNull(invalidTrip);
	}

	@Test
	public void testGetTrip() {
		Long tripId = tripService.getAllTrips().get(2).getId();
		Trip trip = tripService.getTrip(tripId);
		String expectedDest = "Barcelona";
		Assert.assertEquals(expectedDest, trip.getDestination());
	}

	@Test
	public void testGetTripsByUserAndBetweenDates() {
		Long userId = userService.getAllUsers().get(0).getId();
		List<Trip> tripList = tripService.getFilteredTrips(null, DateUtils.getDate(2022, 3, 1),
				DateUtils.getDate(2022, 3, 11), userId);

		int expectedTrips = 1;
		int actualTrips = tripList.size();
		Assert.assertEquals(expectedTrips, actualTrips);

		String expectedDest = "Alexandria";
		String actualDest = tripList.get(0).getDestination();
		Assert.assertEquals(expectedDest, actualDest);
	}

	@Test
	public void testGetTripsBetweenDates() {
		List<Trip> tripList = tripService.getFilteredTrips(null, DateUtils.getDate(2022, 3, 1),
				DateUtils.getDate(2022, 8, 1), null);

		int expectedTrips = 4;
		int actualTrips = tripList.size();
		Assert.assertEquals(expectedTrips, actualTrips);
	}

	@Test
	public void testGetTripsByDestination() {
		List<Trip> tripList = tripService.getFilteredTrips("Alexandria", null, null, null);

		int expectedTrips = 2;
		int actualTrips = tripList.size();
		Assert.assertEquals(expectedTrips, actualTrips);
	}

	@Test
	public void testGetTripsByDestinationAndUser() {
		Long userId = userService.getAllUsers().get(1).getId();
		List<Trip> tripList = tripService.getFilteredTrips("Alexandria", null, null, userId);

		int expectedTrips = 1;
		int actualTrips = tripList.size();
		Assert.assertEquals(expectedTrips, actualTrips);

		String expectedUsername = "barca";
		String actualUsername = tripList.get(0).getUser().getUsername();
		Assert.assertEquals(expectedUsername, actualUsername);

		// Test invalid destination
		List<Trip> invalidList1 = tripService.getFilteredTrips("Wrong Destination", null, null, userId);
		Assert.assertTrue(invalidList1.isEmpty());
	}

	@Test
	public void testGetUserTrips() {
		Long userId = userService.getAllUsers().get(2).getId();
		List<Trip> tripList = tripService.getFilteredTrips(null, null, null, userId);

		int expectedTrips = 1;
		int actualTrips = tripList.size();
		Assert.assertEquals(expectedTrips, actualTrips);

		String expectedDest = "Paris";
		String actualDest = tripList.get(0).getDestination();
		Assert.assertEquals(expectedDest, actualDest);

		// Test invalid userId
		List<Trip> invalidList2 = tripService.getFilteredTrips(null, null, null, -100L);
		Assert.assertTrue(invalidList2.isEmpty());
	}
}
