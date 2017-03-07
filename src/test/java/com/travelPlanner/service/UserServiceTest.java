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
import com.travelPlanner.exception.UsernameAlreadyExistsException;
import com.travelPlanner.persist.dao.UserDao;
import com.travelPlanner.persist.entity.User;
import com.travelPlanner.persist.entity.UserRole;
import com.travelPlanner.service.UserService;
import com.travelPlanner.service.impl.UserServiceImpl;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = { SpringBootConfig.class, UserServiceImpl.class, UserDao.class })
@EntityScan(basePackages = "com.travelPlanner.persist.entity")
public class UserServiceTest {

	@Autowired
	private UserService<Long> userService;

	@Before
	public void testCreateUser() {
		// Create first user
		User userOne = new User("John", "Snow", "admin", "admin", UserRole.ADMIN);
		boolean expected = true;
		boolean actual = userService.createUser(userOne);
		Assert.assertEquals(expected, actual);

		// Create second user
		User userTwo = new User("Andres", "Iniesta", "barca", "barcelona", UserRole.USER_MANAGER);
		actual = userService.createUser(userTwo);
		Assert.assertEquals(expected, actual);

		// Create third user
		User userThree = new User("Lionel", "Messi", "leo", "thiago", UserRole.REGULAR_USER);
		actual = userService.createUser(userThree);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testInvalidCreateUserNullUsername() {
		// Trying to create user with null username
		User userOne = new User("Invalid", "User", null, "noUsername", UserRole.ADMIN);
		boolean expected = false;
		boolean actual = userService.createUser(userOne);
		Assert.assertEquals(expected, actual);
	}

	@Test(expected = UsernameAlreadyExistsException.class)
	public void testInvalidCreateUserExistingUsername() {
		// Trying to create user with an existing username
		User userOne = new User("Hazem", "Emam", "barca", "existingUsername", UserRole.ADMIN);
		userService.createUser(userOne);
	}

	@Test
	public void testGetAll() {
		List<User> userList = userService.getAllUsers();
		int expectedUsers = 3;
		int actualUsers = userList.size();
		Assert.assertEquals(expectedUsers, actualUsers);
	}

	@Test
	public void testUpdateUser() {
		// Retrieved the second user
		User user = userService.getAllUsers().get(1);
		String expectedPassword = "barcelona";
		Assert.assertEquals(expectedPassword, user.getPassword());

		// Update user's password
		user.setPassword("barcelonaNewPassword");
		userService.updateUser(user);

		User updatedUser = userService.getAllUsers().get(1);
		expectedPassword = "barcelonaNewPassword";
		Assert.assertEquals(expectedPassword, updatedUser.getPassword());
	}

	@Test(expected = UsernameAlreadyExistsException.class)
	public void testInvalidUpdateUser() {
		// Retrieved the first user
		User user = userService.getAllUsers().get(0);
		User newUser = User.copyUser(user);
		String expectedUsername = "admin";
		Assert.assertEquals(expectedUsername, newUser.getUsername());

		// Update user's username with an existing one!
		newUser.setUsername("leo");
		userService.updateUser(newUser);
	}

	@Test
	public void testDeleteUser() {
		long id = userService.getAllUsers().get(0).getId();

		// Test if user is removed successfully
		String expectedUsername = "admin";
		User removedUser = userService.deleteUser(id);
		Assert.assertEquals(expectedUsername, removedUser.getUsername());

		// Test number of remaining users
		List<User> users = userService.getAllUsers();
		int expectedUsers = 2;
		int actualUsers = users.size();
		Assert.assertEquals(expectedUsers, actualUsers);
	}

	@Test
	public void testInvalidRemoveUser() {
		long invalidId = 120;

		// Test removing user with invalid Id
		User removedUser = userService.deleteUser(invalidId);
		Assert.assertNull(removedUser);
	}

	@Test
	public void testGetUserByUsername() {
		User user = userService.getUserByUsername("leo");
		String expectedLastName = "Messi";
		Assert.assertEquals(expectedLastName, user.getLastName());
	}

	@Test
	public void testInvalidGetUserByUsername() {
		User user = userService.getUserByUsername("Ronaldo");
		Assert.assertNull(user);
	}
}
