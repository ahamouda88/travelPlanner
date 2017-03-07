package com.travelPlanner.constants;

/**
 * An interface that contains url path constants
 */
public interface PathConstants {

	public final static String SEARCH_PATH = "/search";
	public final static String ID_PATH = "/{id}";

	// User Constants
	public final static String USERS_PATH = "/api/users";
	public final static String USERNAME_PATH = "/username/{username}";
	public final static String CURRENT_USER = "/currentuser";

	// Trip Constants
	public final static String TRIPS_PATH = "/api/trips";
	public final static String COUNTRIES_PATH = "/countries";

}
