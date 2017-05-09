package com.travelPlanner.rest.controller;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travelPlanner.constants.PathConstants;
import com.travelPlanner.exception.EntityDoesnotExistException;
import com.travelPlanner.persist.entity.Trip;
import com.travelPlanner.service.TripService;

/**
 * This class is a Rest Controller for handling the trip actions, and handling the trip end-points
 */
@RestController
@RequestMapping(value = PathConstants.TRIPS_PATH)
public class TripController {

	@Autowired
	private TripService<Long> tripService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Trip> addTrip(@RequestBody Trip trip) {
		if (trip.getUser() == null) {
			throw new NullPointerException("User for trip Id: " + trip.getId() + " can't be null!");
		}

		boolean inserted = tripService.createTrip(trip);
		return inserted ? new ResponseEntity<Trip>(trip, HttpStatus.CREATED)
				: new ResponseEntity<>(trip, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Trip> updateTrip(@RequestBody Trip trip) {
		if (trip.getUser() == null) {
			throw new NullPointerException("User for trip Id: " + trip.getId() + " can't be null!");
		}

		boolean updated = tripService.updateTrip(trip);
		return updated ? new ResponseEntity<Trip>(trip, HttpStatus.OK)
				: new ResponseEntity<Trip>(trip, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Trip> deleteTrip(@RequestParam("id") Long tripId) {
		Trip removedTrip = tripService.deleteTrip(tripId);
		return validateAndGetTripEntity(removedTrip, String.format("Trip with Id %d doesn't exist!", tripId));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Trip>> getAllTrips() {
		return new ResponseEntity<>(tripService.getAllTrips(), HttpStatus.OK);
	}

	@RequestMapping(value = PathConstants.ID_PATH, method = RequestMethod.GET)
	public ResponseEntity<Trip> getTripById(@PathVariable(name = "id") Long tripId) {
		return validateAndGetTripEntity(tripService.getTrip(tripId),
				String.format("Trip with Id %d doesn't exist!", tripId));
	}

	@RequestMapping(value = PathConstants.SEARCH_PATH, method = RequestMethod.GET)
	public ResponseEntity<List<Trip>> getFilteredTrips(
			@RequestParam(name = "destination", required = false) String destination,
			@RequestParam(name = "userid", required = false) Long userId,
			@RequestParam(name = "startdate", required = false) Date startDate,
			@RequestParam(name = "enddate", required = false) Date endDate) {
		List<Trip> trips = tripService.getFilteredTrips(destination, startDate, endDate, userId);

		return new ResponseEntity<List<Trip>>(trips, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = PathConstants.COUNTRIES_PATH, method = RequestMethod.GET)
	public ResponseEntity<List<String>> getCountries() throws Exception {
		List<String> countries = new ArrayList<>();

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader("./src/main/resources/countries.json"));

		JSONArray countryList = (JSONArray) obj;
		Iterator<String> iterator = countryList.iterator();
		while (iterator.hasNext()) {
			countries.add(iterator.next());
		}
		return new ResponseEntity<>(countries, HttpStatus.OK);
	}

	private <T> ResponseEntity<T> validateAndGetTripEntity(T data, String message) {
		if (data == null) {
			throw new EntityDoesnotExistException(message);
		}
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
}
