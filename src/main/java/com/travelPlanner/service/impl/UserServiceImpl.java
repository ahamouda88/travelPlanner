package com.travelPlanner.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travelPlanner.exception.UsernameAlreadyExistsException;
import com.travelPlanner.persist.dao.UserDao;
import com.travelPlanner.persist.entity.User;
import com.travelPlanner.service.UserService;

/**
 * Implementation of {@link UserService}
 */
@Service
@Transactional
public class UserServiceImpl implements UserService<Long> {

	@Autowired
	private UserDao userDao;

	/**
	 * @see UserService#createUser(User)
	 */
	@Override
	public boolean createUser(User user) throws UsernameAlreadyExistsException {
		if (!User.isValid(user)) return false;

		if (usernameExists(user.getUsername())) {
			throw new UsernameAlreadyExistsException(user.getUsername());
		}
		return userDao.create(user);
	}

	/**
	 * @see UserService#updateUser(User)
	 */
	@Override
	public boolean updateUser(User user) throws UsernameAlreadyExistsException {
		if (!User.isValid(user) || user.getId() == null) return false;

		User userOldData = userDao.find(user.getId());

		if (userOldData == null) return false;

		// If the user name is being updated!
		if (!userOldData.getUsername().equals(user.getUsername())) {
			if (usernameExists(user.getUsername())) {
				throw new UsernameAlreadyExistsException(user.getUsername());
			}
		}
		return userDao.update(user);
	}

	/**
	 * @see UserService#deleteUser(java.io.Serializable)
	 */
	@Override
	public User deleteUser(Long userId) {
		User user = this.getUser(userId);

		return user == null ? null : userDao.remove(user);
	}

	/**
	 * @see UserService#getUser(java.io.Serializable)
	 */
	@Override
	public User getUser(Long userId) {
		return userId == null ? null : userDao.find(userId);
	}

	/**
	 * @see UserService#getAllUsers()
	 */
	@Override
	public List<User> getAllUsers() {
		return userDao.getAll();
	}

	/**
	 * @see UserService#getUserByUsername(String)
	 */
	@Override
	public User getUserByUsername(String username) {
		return username == null ? null : userDao.findByUsername(username);
	}

	/*
	 * Check if user with the given user name exists
	 */
	private boolean usernameExists(String username) {
		return this.getUserByUsername(username) == null ? false : true;
	}
}
