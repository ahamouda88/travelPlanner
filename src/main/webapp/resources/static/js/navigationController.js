(function() {
	var application = angular.module('application'),
		currentUserCookie = 'CURRENT_USER';

	// Navigation Controller
	function NavigationController($scope, $rootScope, $location, $cookies, UserService, $templateCache) {
		
		var authenticate = function(credentials, callback) {
			$templateCache.removeAll();
			// If new credentials are added!
			if(credentials){
				var response = UserService.loginUser(credentials),
					tmpData;
				response.success(function(data) {					
					tmpData = data;
					tmpData.password = null;
		    		$cookies.put(currentUserCookie, JSON.stringify(tmpData));
		    		$rootScope.authenticated = true;
		    		$rootScope.currentUser = data;
			    	callback && callback();
			    });
				response.error(function() {
			    	$cookies.remove(currentUserCookie);
			    	$rootScope.authenticated = false;
			    	$rootScope.currentUser = null;
			    	callback && callback();
			    });
			}else{
				// Else try to get information about current user, in case of page reload!
				var currentUser = $cookies.get(currentUserCookie);
				if(currentUser) {
					$rootScope.currentUser = JSON.parse(currentUser);
					var data = {
						username : $rootScope.currentUser.username,
						password : $rootScope.currentUser.password
					};
					UserService.loginUser(data);
					$rootScope.authenticated = true;
				}
			}
		}
		authenticate();
		
		$scope.credentials = {};
		// A login function
		$scope.loginUser = function() {
			authenticate($scope.credentials, function() {
				if ($rootScope.authenticated) {
					$location.path("/");
					$scope.credentials = {};
					$scope.loginError = false;
		        } else {
		        	$scope.credentials.password = null;
		        	$scope.loginError = true;
		        }
			});
		};
		
		// A logout function
		$scope.logoutUser = function() {
			UserService.logoutUser().success(function() {
            	$rootScope.authenticated = false;
            	$rootScope.currentUser = null;
                $location.path("/login");
            }).error(function(data) {
                console.log("Logout failed");
            });
        };
	}
	
	application.controller('NavigationController', NavigationController);
})();