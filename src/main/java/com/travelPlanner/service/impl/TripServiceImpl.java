package com.travelPlanner.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travelPlanner.model.request.TripSearchRequest;
import com.travelPlanner.persist.dao.TripDao;
import com.travelPlanner.persist.dao.UserDao;
import com.travelPlanner.persist.entity.Trip;
import com.travelPlanner.persist.entity.User;
import com.travelPlanner.service.TripService;

/**
 * Implementation of {@link TripService}
 */
@Service
@Transactional
public class TripServiceImpl implements TripService<Long> {

	@Autowired
	private TripDao tripDao;

	@Autowired
	private UserDao userDao;

	/**
	 * @see TripService#createTrip(Trip)
	 */
	@Override
	public boolean createTrip(Trip trip) {
		if (!Trip.isValid(trip) || trip.getUser() == null || trip.getUser().getId() == null) return false;

		User user = userDao.find(trip.getUser().getId());
		if (user == null) return false;
		
		List<Trip> trips = user.getTrips();
		if (trips == null) trips = new ArrayList<>();
		trips.add(trip);

		user.setTrips(trips);
		return true;
	}

	/**
	 * @see TripService#updateTrip(Trip)
	 */
	@Override
	public boolean updateTrip(Trip trip) {
		if (!Trip.isValid(trip) || trip.getId() == null) return false;

		return tripDao.update(trip);
	}

	/**
	 * @see TripService#deleteTrip(java.io.Serializable)
	 */
	@Override
	public Trip deleteTrip(Long tripId) {
		Trip trip = this.getTrip(tripId);

		if (trip == null) return null;

		Trip removedTrip = tripDao.remove(trip);

		if (removedTrip == null) return null;
		// Remove trip from the parent association (user)
		removedTrip.getUser().getTrips().remove(removedTrip);

		return removedTrip;
	}

	/**
	 * @see TripService#getTrip(java.io.Serializable)
	 */
	@Override
	public Trip getTrip(Long tripId) {
		return tripId == null ? null : tripDao.find(tripId);
	}

	/**
	 * @see TripService#getAllTrips()
	 */
	@Override
	public List<Trip> getAllTrips() {
		return tripDao.getAll();
	}

	/**
	 * @see TripService#getFilteredTrips(String, Date, Date, Long)
	 */
	@Override
	public List<Trip> getFilteredTrips(String destination, Date startDate, Date endDate, Long userId) {
		TripSearchRequest searchRequest = TripSearchRequest.builder().destination(destination).startDate(startDate)
				.endDate(endDate).userId(userId).build();
		return tripDao.searchTrips(searchRequest);
	}

}
