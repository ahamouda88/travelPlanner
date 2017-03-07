package com.travelPlanner.service.impl;

import static org.springframework.util.StringUtils.isEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.travelPlanner.model.CurrentUserDetails;
import com.travelPlanner.persist.dao.UserDao;
import com.travelPlanner.persist.entity.User;

/**
 * Implementation of Spring's {@link UserDetailsService}, that represents the current user's data
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (isEmpty(username)) {
			throw new UsernameNotFoundException("Username shouldn't be null or empty!");
		}

		User user = userDao.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User with the username: " + username + ", is not found!");
		}
		return new CurrentUserDetails(user);
	}
}
