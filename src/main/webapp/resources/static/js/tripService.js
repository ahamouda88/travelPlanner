(function() {
	var application = angular.module("application"),
		tripsPath = '/api/trips';

	// Trip Service
	function TripService($http, $filter) {
		// Add or Update Trip
		this.addUpdateTrip = function(data) {
			/*
			 * Check if trip's id is null then make a post request, otherwise
			 * make a put request
			 */
			if (data.id === undefined) {
				return $http.post(tripsPath, data);
			} else {
				return $http.put(tripsPath, data);
			}
		};
		
		// Remove Trip
		this.deleteTrip = function(data) {
			var path = tripsPath + '?id=' + data.id;
			return $http.delete(path);
		};

		// Get All Trips
		this.getAllTrips = function() {
			return $http.get(tripsPath);
		};
		
		// Returns trips filtered by the given data
		this.getFilteredTrips = function(destination, startDate, endDate, userId){
			var path = tripsPath + '/search?';
			if(destination) path += 'destination='+destination;
			
			if(startDate && endDate) {
				path += '&startdate='+ $filter('dateFormat')(startDate) +'&enddate='+ $filter('dateFormat')(endDate);
			}
			
			if(userId) path += '&userid='+userId;
			
			return $http.get(path);
		};
		
		// Returns a list of countries
		this.getCountries = function(){
			return $http.get(tripsPath + '/countries');
		};
		
		// Set target Trip, when row is selected
		var targetTrip = {};
		this.setTargetTrip = function(trip){
			targetTrip = trip;
		};
		
		// Get target Trip, to be displayed on the add/update page
		this.getTargetTrip = function(){
			return targetTrip;
		};
	}

	application.service("TripService", TripService);
})();