package com.travelPlanner.service;

import java.io.Serializable;
import java.util.List;

import com.travelPlanner.exception.UsernameAlreadyExistsException;
import com.travelPlanner.persist.dao.UserDao;
import com.travelPlanner.persist.entity.User;

/**
 * An interface that defines the operations to be performed on {@link User} model, and it should interacts with the
 * User's Dao Layer {@link UserDao}
 * 
 * @param <T>
 *            the model Id's data type
 */
public interface UserService<T extends Serializable> {

	/**
	 * This method creates a {@link User} model, given the user model
	 * 
	 * @param user
	 *            the user that need to be created
	 * @return true if user is created successfully, or false otherwise
	 * @throws UsernameAlreadyExistsException
	 *             if user name already exists
	 */
	public boolean createUser(User user) throws UsernameAlreadyExistsException;

	/**
	 * This method updates a {@link User} model, given the user model
	 * 
	 * @param user
	 *            the user that need to be updated
	 * @return true if user is updated successfully, or false otherwise
	 * @throws UsernameAlreadyExistsException
	 *             if user name already exists
	 */
	public boolean updateUser(User user) throws UsernameAlreadyExistsException;

	/**
	 * This method removes a {@link User} model, based on the given user's id
	 * 
	 * @param userId
	 *            a user's Id
	 * @param <T>
	 *            the user's Id data type
	 * @return the removed {@link User} model if user exists, otherwise it will return <b>null</b>
	 */
	public User deleteUser(T userId);

	/**
	 * This method returns a {@link User} model, based on the given user's id
	 * 
	 * @param userId
	 *            a user's Id
	 * @param <T>
	 *            the user's Id data type
	 * @return a {@link User} model if user exists, otherwise it will return <b>null</b>
	 */
	public User getUser(T userId);

	/**
	 * This method returns a {@link User} model, based on the given username
	 * 
	 * @param username
	 *            a user's username
	 * @return a {@link User} model if user exists, otherwise it will return <b>null</b>
	 */
	public User getUserByUsername(String username);

	/**
	 * This method returns a list of all users
	 * 
	 * @return a list of users
	 */
	public List<User> getAllUsers();

}
