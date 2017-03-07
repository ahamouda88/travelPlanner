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
import com.travelPlanner.persist.dao.UserDao;
import com.travelPlanner.persist.entity.Trip;
import com.travelPlanner.persist.entity.User;
import com.travelPlanner.persist.entity.UserRole;
import com.travelPlanner.utils.DateUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = { SpringBootConfig.class, UserDao.class })
@EntityScan(basePackages = "com.travelPlanner.persist.entity")
public class UserDaoTest {

	@Autowired
	private UserDao userDao;

	@Before
	public void testCreateUser() {
		// Create the first user
		User userOne = new User("John", "Snow", "admin", "admin", UserRole.ADMIN);

		boolean expected = true;
		boolean actualResult = userDao.create(userOne);
		Assert.assertEquals(expected, actualResult);

		// Create the second user
		User userTwo = new User("Lionel", "Messi", "Ahmed", "password", UserRole.REGULAR_USER);

		actualResult = userDao.create(userTwo);
		Assert.assertEquals(expected, actualResult);
	}

	@Test
	public void testInValidCreateUser() {
		// Create Invalid user
		User userOne = new User("Cristiano", "Ronaldo", "admin", "admin", null);

		boolean expected = false;
		boolean actualResult = userDao.create(userOne);
		Assert.assertEquals(expected, actualResult);
	}

	@Test
	public void testUpdateUser() {
		User userOne = userDao.find(1L);

		String expectedPassword = "admin";
		Assert.assertEquals(expectedPassword, userOne.getPassword());

		// Update user with Id=1
		expectedPassword = "newPassword";
		userOne.setPassword(expectedPassword);

		// Test if user is updated successfully
		boolean expected = true;
		boolean actualResult = userDao.update(userOne);
		Assert.assertEquals(expected, actualResult);

		// Test updated password
		User updatedUser = userDao.find(1L);
		Assert.assertEquals(expectedPassword, updatedUser.getPassword());
	}

	@Test
	public void testRemoveUser() {
		// Get last added user
		long id = userDao.getAll().get(1).getId();
		User userToBeRemoved = userDao.find(id);

		String expectedUsername = "Ahmed";
		Assert.assertEquals(expectedUsername, userToBeRemoved.getUsername());

		// Test if user is removed successfully
		User removedUser = userDao.remove(userToBeRemoved);
		Assert.assertEquals(expectedUsername, removedUser.getUsername());

		// Test number of remaining users
		List<User> users = userDao.getAll();
		int expectedUsers = 1;
		int actualUsers = users.size();
		Assert.assertEquals(expectedUsers, actualUsers);
	}

	@Test
	public void testPersistingChild() {
		User user = new User("Ahmed", "Hamouda", "NEW USER", "nice", UserRole.USER_MANAGER);

		Trip trip = new Trip("New York City", DateUtils.getDate(2022, 5, 1), DateUtils.getDate(2022, 6, 1), "Nice Trip");
		trip.setUser(user);

		List<Trip> list = new ArrayList<>();
		list.add(trip);
		user.setTrips(list);

		userDao.create(user);
		Assert.assertTrue(trip.getId() > 0);
	}

	@Test
	public void testInValidFind() {
		// Find user with invalid Id
		User user = userDao.find(100L);
		Assert.assertNull(user);
	}

	@Test
	public void testGetAll() {
		List<User> users = userDao.getAll();
		int expectedUsers = 2;
		int actualUsers = users.size();

		Assert.assertEquals(expectedUsers, actualUsers);
	}

	@Test
	public void testFindByUsername() {
		User user = userDao.findByUsername("ahmed");
		String expectedUsername = "Ahmed";

		Assert.assertEquals(expectedUsername, user.getUsername());
	}
}
