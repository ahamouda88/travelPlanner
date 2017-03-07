(function() {
	var application = angular.module("application");

	// Trip Controller
	function TripController($scope, $rootScope, TripService, UserService, PagerService) {
		// Check if we need to request all trips or only user's trips
		var currentUserId = ($rootScope.currentUser && $rootScope.currentUser.role !== 'ADMIN') ? $rootScope.currentUser.id : null;
		
		// Add or Update Trip
		$scope.addUpdateTrip = function() {
			$scope.errorMessage = null;
			var dataObj = {
				id : $scope.trip.id,
				destination : $scope.trip.destination,
				startDate : $scope.trip.startDate,
				endDate : $scope.trip.endDate,
				comment : $scope.trip.comment,
				user : $scope.trip.user,
				version : $scope.trip.version
			};
			
			// Verify if it is an insert or update request
			if ($scope.trip.id === undefined) {
				$scope.insert = true;
				if(!dataObj.user){
					dataObj.user = $rootScope.currentUser;
				}
			} else {
				$scope.insert = false;
			}

			// Start and End Dates validation
			if (!validDates($scope.trip.startDate, $scope.trip.endDate)) {
				$scope.trip.response = {
					errorCode : -1,
					message : 'Start and End dates should be after Today\'s date, and End date should be after the Start Date!'
				};
			} else {
				if(dataObj.user === undefined){
					$scope.trip.response = {
						errorCode : -1,
						message : "User can't be null!"
					};
				}else{
					var response = TripService.addUpdateTrip(dataObj);
					response.success(function(data) {
						$scope.trip = {};
						$scope.trip.response = data;
					});
					response.error(function(data) {
						$scope.trip.response = data;
						$scope.insert = false;
					});
				};
			}
		};

		// Delete Trip
		$scope.deleteTrip = function(trip) {
			if (confirm('Are you sure you want to delete trip with Id: '
					+ trip.id + ' ?')) {
				var response = TripService.deleteTrip(trip).then(function() {
					$scope.trips = $scope.getAllTrips();
					$scope.deletedTrip = trip;
				});
			}
		};

		// Set target Trip, when row is selected
		$scope.setTargetTrip = function(trip) {
			trip.startDate = new Date(trip.startDate);
			trip.endDate = new Date(trip.endDate);
			TripService.setTargetTrip(trip);
		};

		// Get target Trip, to be displayed on the add/update page
		$scope.getTargetTrip = function() {
			$scope.trip = TripService.getTargetTrip();
			var emptyTrip = {};
			TripService.setTargetTrip(emptyTrip);
		};

		// Get All Trips
		$scope.getAllTrips = function() {
			var response;
			
			if(currentUserId){
				response = TripService.getFilteredTrips(null, null, null, currentUserId);
			}else{
				response = TripService.getAllTrips();
			}
			response.success(function(data) {
				$scope.trips = data;
				$scope.tripSet = removeDupDestinations(data);
				setUpPagination(data);
			});
		};

		// Get Countries
		$scope.getCountries = function() {
			var response = TripService.getCountries();
			response.success(function(data) {
				$scope.countries = data;
			});
		};

		/*
		 * Method to process filter and contains logic for which service method
		 * is being called
		 */
		$scope.filterTrips = function() {
			var destination = $scope.filter.trip ? $scope.filter.trip.destination : null;
				startDate = $scope.filter.startDate,
				endDate = $scope.filter.endDate,
				response = TripService.getFilteredTrips(destination, startDate, endDate, currentUserId);
				
			response.success(function(data) {
				$scope.trips = data;
				setUpPagination(data);
			});
		};
		
		// This function loads all next month's trips
		$scope.getNextMonthTrips = function(){
			var startDate,
				endDate,
				response,
				startMonth,
				endMonth,
				now = new Date();
			
			if(now.getMonth() === 11){
				startMonth = 0;
				endMonth = 1;
			}else{
				startMonth = now.getMonth() + 1;
				endMonth = now.getMonth() + 1 === 11 ? 0 : now.getMonth() + 2;
			}
			
			startDate = new Date(now.getFullYear(), startMonth, 1);
			endDate = new Date(now.getFullYear(), endMonth, 0);
			
			response = TripService.getFilteredTrips(null, startDate, endDate, currentUserId);
			response.success(function(data) {
				$scope.trips = data;
			});
		};
		
		// This function is used to print an html element based on it's Id
		$scope.print = function(elementId){
			var printContents = document.getElementById(elementId).innerHTML,
				popupWin = window.open('', '_blank', 'width=300,height=300');
			popupWin.document.open();
			popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
			popupWin.document.close();
		};

		$scope.initTripPage = function() {
			$scope.getCountries();
			$scope.getTargetTrip();
		};
		
		$scope.setNext = function() {
			$scope.currentPage = $scope.tablePagination.setNext();
		};
		
		$scope.setPrev = function() {
			$scope.currentPage = $scope.tablePagination.setPrev();
		}
		
		// Setting up pagination
		function setUpPagination(data){
			$scope.tablePagination = PagerService.tablePagination(data, 5);
			$scope.currentPage = $scope.tablePagination.currentPage;
		}
	}
	
	// Function to remove duplicate destinations
	function removeDupDestinations(data) {
		var uniqueDestinations = [],
			finalTrips = [];
		for (var key in data) {
			var trip = data[key];
			if (uniqueDestinations.indexOf(trip.destination) == -1) {
				uniqueDestinations.push(trip.destination);
				finalTrips.push(trip);
			}
		}
		return finalTrips;
	}

	// Function to do the FE validation for start and end dates
	function validDates(startDate, endDate) {
		// Get today's date
		var todaysDate = new Date();

		return startDate <= endDate && startDate.setHours(0, 0, 0, 0) >= todaysDate.setHours(0, 0, 0, 0)
									&& startDate.setHours(0, 0, 0, 0) >= todaysDate.setHours(0, 0, 0, 0);
	}

	application.controller('TripController', TripController);
})();