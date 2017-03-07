(function() {
	var application = angular.module('application', [ 'ngRoute', 'ngCookies' ]);

	// Handle routes
	application.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/', {
			templateUrl : '/all/view/home',
			resolve : {
				factory : checkRouting
			}
		}).when('/login', {
			templateUrl : '/all/view/login'
		}).when('/user', {
			templateUrl : '/all/view/user'
		}).when('/users', {
			templateUrl : '/usrmgr/view/users',
			resolve : {
				factory : checkRouting
			}
		}).when('/trip', {
			templateUrl : '/usr/view/trip',
			resolve : {
				factory : checkRouting
			}
		}).when('/trips', {
			templateUrl : '/usr/view/trips',
			resolve : {
				factory : checkRouting
			}
		}).when('/trips/next-month', {
			templateUrl : '/usr/view/trips/next-month',
			resolve : {
				factory : checkRouting
			}
		}).otherwise({
			redirectTo : '/'
		});
	} ]);

	// Check first if currentUser is authenticated, before doing the route
	var checkRouting = function($rootScope, $location) {
		if ($rootScope.authenticated) {
			return true;
		} else {
			$location.path("/login");
			return false;
		}
	};
	
	// Create a date format filter
	application.filter('dateFormat', function ($filter) {
		return function (date) {
			return date === undefined ? '' : $filter('date')(date, 'MM-dd-yyyy');
		}	   
	});
})();
