package com.travelPlanner.rest.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travelPlanner.constants.PathConstants;
import com.travelPlanner.exception.EntityDoesnotExistException;
import com.travelPlanner.model.CurrentUserDetails;
import com.travelPlanner.persist.entity.User;
import com.travelPlanner.service.UserService;

/**
 * This class is a Rest Controller for handling the user actions, and handling the user end-points
 */
@RestController
@RequestMapping(value = PathConstants.USERS_PATH)
public class UserController {

	@Autowired
	private UserService<Long> userService;

	@RequestMapping(value = PathConstants.CURRENT_USER, method = RequestMethod.GET)
	public User user(Principal user) {
		try {
			CurrentUserDetails currentUser = (CurrentUserDetails) ((Authentication) user).getPrincipal();
			return currentUser.getUser();
		} catch (Exception ex) {
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		boolean inserted = userService.createUser(user);

		return inserted ? new ResponseEntity<User>(user, HttpStatus.CREATED)
				: new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		boolean updated = userService.updateUser(user);
		return updated ? new ResponseEntity<User>(user, HttpStatus.OK)
				: new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<User> deleteUser(@RequestParam("id") Long userId) {
		User removedUser = userService.deleteUser(userId);
		return validateAndGetUserEntity(removedUser, "User with user Id " + userId + " doesn't exist!");
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers() {
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}

	@RequestMapping(value = PathConstants.ID_PATH, method = RequestMethod.GET)
	public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
		return validateAndGetUserEntity(userService.getUser(userId), "User with user Id " + userId + " doesn't exist!");
	}

	@RequestMapping(value = PathConstants.USERNAME_PATH, method = RequestMethod.GET)
	public ResponseEntity<User> getUserByUserName(@PathVariable(name = "username") String username) {
		return validateAndGetUserEntity(userService.getUserByUsername(username), username);
	}

	private <T> ResponseEntity<T> validateAndGetUserEntity(T data, String message) {
		if (data == null) {
			throw new EntityDoesnotExistException(message);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
}
