package com.travelPlanner.persist.dao;

import java.util.ArrayList;
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
import com.travelPlanner.model.request.TripSearchRequest;
import com.travelPlanner.persist.dao.TripDao;
import com.travelPlanner.persist.dao.UserDao;
import com.travelPlanner.persist.entity.Trip;
import com.travelPlanner.persist.entity.User;
import com.travelPlanner.persist.entity.UserRole;
import com.travelPlanner.utils.DateUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = { SpringBootConfig.class, TripDao.class, UserDao.class })
@EntityScan(basePackages = "com.travelPlanner.persist.entity")
public class TripDaoTest {

	@Autowired
	private TripDao tripDao;

	@Autowired
	private UserDao userDao;

	@Before
	public void testCreateTrip() {
		User user = new User("Quynh", "To Tuan", "Quinni", "nice", UserRole.ADMIN);
		userDao.create(user);
		boolean expectedCheck = true;

		// Create Trip 1
		Trip trip = new Trip("London", DateUtils.getDate(2017, 5, 1), DateUtils.getDate(2017, 6, 1), "Nice Trip");
		trip.setUser(user);

		boolean actualCheck = tripDao.create(trip);
		Assert.assertEquals(expectedCheck, actualCheck);

		// Create trip 2
		Trip trip2 = new Trip("Barcelona", DateUtils.getDate(2017, 7, 1), DateUtils.getDate(2017, 9, 1), "Second Trip");
		trip2.setUser(user);

		actualCheck = tripDao.create(trip2);
		Assert.assertEquals(expectedCheck, actualCheck);

		List<Trip> trips = new ArrayList<>();
		trips.add(trip);
		trips.add(trip2);
		user.setTrips(trips);
		userDao.update(user);
	}

	@Test
	public void testUpdateTrip() {
		List<Trip> allTrips = tripDao.getAll();
		long tripId = allTrips.get(1).getId();
		Trip trip = tripDao.find(tripId);
		trip.setComment("My new comment");

		boolean expected = true;
		boolean actualResult = tripDao.update(trip);
		Assert.assertEquals(expected, actualResult);

		Trip updatedTrip = tripDao.find(tripId);
		String expectedComment = "My new comment";
		Assert.assertEquals(expectedComment, updatedTrip.getComment());
	}

	@Test
	public void testRemoveTrip() {
		List<Trip> allTrips = tripDao.getAll();
		long tripId = allTrips.get(1).getId();
		Trip trip = tripDao.find(tripId);

		Trip removedTrip = tripDao.remove(trip);
		// Should remove trip from parent's collection
		removedTrip.getUser().getTrips().remove(removedTrip);

		Assert.assertNotNull(removedTrip);

		// Test size of the list after removing a trip
		List<Trip> newTripList = tripDao.getAll();

		int expectedSize = 1;
		Assert.assertEquals(expectedSize, newTripList.size());
	}

	@Test
	public void testFindTrip() {
		// Test total number of trips
		List<Trip> allTrips = tripDao.getAll();
		int expectedSize = 2;
		Assert.assertEquals(expectedSize, allTrips.size());

		// Test the find method
		Trip trip = tripDao.find(allTrips.get(0).getId());
		String expectedComment = "Nice Trip";
		Assert.assertEquals(expectedComment, trip.getComment());
	}

	@Test
	public void testGetUserTrips() {
		// Create a user
		User user = new User("Ahmed", "Hamouda", "Ahamouda", "password", UserRole.ADMIN);

		// Create a trip
		Trip trip = new Trip("Alexandria", DateUtils.getDate(2017, 7, 1), DateUtils.getDate(2017, 9, 1), "First Trip");
		trip.setUser(user);

		List<Trip> trips = new ArrayList<>();
		trips.add(trip);
		user.setTrips(trips);
		userDao.create(user);

		List<Trip> userTrips = tripDao.searchTrips(TripSearchRequest.builder().userId(user.getId()).build());
		int expectedSize = 1;
		Assert.assertEquals(expectedSize, userTrips.size());

		String expectedDestination = "Alexandria";
		Assert.assertEquals(expectedDestination, userTrips.get(0).getDestination());
	}

	@Test
	public void testFindByDestination() {
		List<Trip> trips = tripDao.searchTrips(TripSearchRequest.builder().destination("Barcelona").build());
		User user = trips.get(0).getUser();

		// Test the number of returned trips
		int expectedSize = 1;
		Assert.assertEquals(expectedSize, trips.size());

		// Test the user associated with the returned trip
		String expectedUsername = "Quinni";
		Assert.assertEquals(expectedUsername, user.getUsername());
	}

	@Test
	public void testFindByDestinationAndUser() {
		User user = userDao.findByUsername("Quinni");

		List<Trip> trips = tripDao
				.searchTrips(TripSearchRequest.builder().destination("London").userId(user.getId()).build());

		// Test the number of returned trips
		int expectedSize = 1;
		Assert.assertEquals(expectedSize, trips.size());

		// Test the user associated with the returned trip
		String expectedUsername = "Quinni";
		Assert.assertEquals(expectedUsername, user.getUsername());
	}

	@Test
	public void testFindBetweenDates() {

		List<Trip> trips = tripDao.searchTrips(TripSearchRequest.builder().startDate(DateUtils.getDate(2017, 7, 1))
				.endDate(DateUtils.getDate(2017, 10, 1)).build());

		// Test the number of returned trips
		int expectedSize = 1;
		Assert.assertEquals(expectedSize, trips.size());
	}

	@Test
	public void testFindByDestinationUserAndBetweenDates() {
		// Test valid destination, user and date range
		User user = userDao.findByUsername("Quinni");
		TripSearchRequest searchRequest = TripSearchRequest.builder().destination("London")
				.startDate(DateUtils.getDate(2017, 5, 1)).endDate(DateUtils.getDate(2017, 6, 1)).userId(user.getId())
				.build();
		List<Trip> trips = tripDao.searchTrips(searchRequest);

		int expectedSize = 1;
		Assert.assertEquals(expectedSize, trips.size());

		// Test invalid date range
		TripSearchRequest invalidSearchRequest = TripSearchRequest.builder().destination("London")
				.startDate(DateUtils.getDate(2017, 7, 1)).endDate(DateUtils.getDate(2017, 8, 1)).userId(user.getId())
				.build();
		List<Trip> invalidTrips = tripDao.searchTrips(invalidSearchRequest);

		expectedSize = 0;
		Assert.assertEquals(expectedSize, invalidTrips.size());
	}

	@Test
	public void testFindByDestinationAndBetweenDates() {
		// Test valid destination, and date range
		TripSearchRequest searchRequest = TripSearchRequest.builder().destination("Barcelona")
				.startDate(DateUtils.getDate(2017, 7, 1)).endDate(DateUtils.getDate(2017, 9, 1)).build();
		List<Trip> trips = tripDao.searchTrips(searchRequest);

		int expectedSize = 1;
		Assert.assertEquals(expectedSize, trips.size());
	}

	@Test
	public void testInvalidFindBetweenDates() {
		TripSearchRequest searchRequest = TripSearchRequest.builder().startDate(DateUtils.getDate(2015, 7, 1))
				.endDate(DateUtils.getDate(2016, 10, 1)).build();
		List<Trip> trips = tripDao.searchTrips(searchRequest);

		// Test the number of returned trips
		int expectedSize = 0;
		Assert.assertEquals(expectedSize, trips.size());
	}

	@Test
	public void testFindBetweenDatesAndByUser() {
		User user = new User("Omar", "Ibrahim", "Eleven", "noPassword", UserRole.ADMIN);
		userDao.create(user);

		// Create Trip 1
		Trip trip = new Trip("Sydney", DateUtils.getDate(2017, 8, 1), DateUtils.getDate(2017, 9, 1), "Australia");
		trip.setUser(user);
		tripDao.create(trip);

		// Create Trip 1
		Trip trip2 = new Trip("Roma", DateUtils.getDate(2017, 8, 14), DateUtils.getDate(2017, 9, 1), "Italy");
		trip2.setUser(user);
		tripDao.create(trip2);

		List<Trip> trips = new ArrayList<>();
		trips.add(trip);
		trips.add(trip2);
		user.setTrips(trips);
		userDao.update(user);

		TripSearchRequest searchRequest = TripSearchRequest.builder().startDate(DateUtils.getDate(2017, 8, 1))
				.endDate(DateUtils.getDate(2017, 8, 4)).userId(user.getId()).build();
		List<Trip> filteredTrips = tripDao.searchTrips(searchRequest);

		// Test the number of returned trips
		int expectedSize = 1;
		Assert.assertEquals(expectedSize, filteredTrips.size());
	}
}
