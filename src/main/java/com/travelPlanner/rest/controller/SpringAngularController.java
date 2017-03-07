package com.travelPlanner.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.travelPlanner.constants.PageConstants;

/**
 * This controller class returns the name of the view file corresponding to every angular url
 */
@Controller
public class SpringAngularController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String appPage() {
		return PageConstants.APP_PAGE;
	}

	@RequestMapping(value = "/all/view/home", method = RequestMethod.GET)
	public String homePage() {
		return PageConstants.HOME_PAGE;
	}

	// This method is for the loginPage in the security config
	@RequestMapping(value = { "/all/view/login", "/logout" }, method = RequestMethod.GET)
	public String loginPage() {
		return PageConstants.LOGIN_PAGE;
	}

	@RequestMapping(value = "/all/view/user", method = RequestMethod.GET)
	public String userPage() {
		return PageConstants.USER_PAGE;
	}

	@RequestMapping(value = "/usrmgr/view/users", method = RequestMethod.GET)
	public String allUsersPage() {
		return PageConstants.ALL_USERS_PAGE;
	}

	@RequestMapping(value = "/usr/view/trip", method = RequestMethod.GET)
	public String tripPage() {
		return PageConstants.TRIP_PAGE;
	}

	@RequestMapping(value = "/usr/view/trips", method = RequestMethod.GET)
	public String allTripsPage() {
		return PageConstants.ALL_TRIPS_PAGE;
	}

	@RequestMapping(value = "/usr/view/trips/next-month", method = RequestMethod.GET)
	public String nextMonthTrips() {
		return PageConstants.NEXT_MONTH_TRIPS_PAGE;
	}
}
