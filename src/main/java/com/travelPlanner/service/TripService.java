package com.travelPlanner.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.travelPlanner.persist.dao.TripDao;
import com.travelPlanner.persist.entity.Trip;

/**
 * An interface that defines the operations to be performed on {@link Trip} model, and it should interacts with the
 * Trip's Dao Layer {@link TripDao}
 */
public interface TripService<T extends Serializable> {

	/**
	 * This method creates a {@link Trip} model, given the trip model
	 * 
	 * @param trip
	 *            the trip that need to be created
	 * @return true if trip is created successfully, or false otherwise
	 */
	public boolean createTrip(Trip trip);

	/**
	 * This method updates a {@link Trip} model, given the trip model
	 * 
	 * @param trip
	 *            the trip that need to be updated
	 * @return true if trip is updated successfully, or false otherwise
	 */
	public boolean updateTrip(Trip trip);

	/**
	 * This method removes a {@link Trip} model, based on the given trip's id
	 * 
	 * @param tripId
	 *            a trip's Id
	 * @param <T>
	 *            the trip's Id data type
	 * @return the removed {@link Trip} model if trip exists, otherwise it will return <b>null</b>
	 */
	public Trip deleteTrip(T tripId);

	/**
	 * This method returns a {@link Trip} model, based on the given trip's id
	 * 
	 * @param tripId
	 *            a trip's Id
	 * @param <T>
	 *            the trip's Id data type
	 * @return a {@link Trip} model if trip exists, otherwise it will return <b>null</b>
	 */
	public Trip getTrip(T tripId);

	/**
	 * This method returns a list of all trips
	 * 
	 * @return a list of trips
	 */
	public List<Trip> getAllTrips();

	/**
	 * This method filters trips based on to the given destination, start date, end date, and user's Id
	 * 
	 * @param destination
	 *            a trip's destination
	 * @param startDate
	 *            a start date
	 * @param endDate
	 *            an end date
	 * @param userId
	 *            a user's id
	 * @return list of trips if exists, otherwise it will return <b>null</b>
	 */
	public List<Trip> getFilteredTrips(String destination, Date startDate, Date endDate, Long userId);
}
