package com.travelPlanner.persist.dao;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.travelPlanner.persist.entity.User;

/**
 * A class that extends {@link AbstractDao}, and it defines the persistence operations to be performed on {@link User}
 */
@Repository
public class UserDao extends AbstractDao<User, Long> {

	public UserDao() {
		super(User.class);
	}

	/**
	 * This method finds a user that is mapped to the given user name
	 * 
	 * @param username
	 *            the user's user name
	 * @return the user mapped to the given user name, or <b>null</b> if user name doesn't exist
	 */
	public User findByUsername(String username) {
		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<User> cq = cb.createQuery(User.class);
			Root<User> from = cq.from(User.class);
			cq.select(from);
			Predicate usernamePredicate = cb.equal(cb.lower(from.get("username")), username.toLowerCase());
			cq.where(usernamePredicate);
			TypedQuery<User> query = entityManager.createQuery(cq);
			return query.getSingleResult();
		} catch (NoResultException ex) {
			logger.debug("User with the following username: " + username + " doesn't exist", ex);
			return null;
		}
	}

}
