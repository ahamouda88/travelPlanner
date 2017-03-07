(function() {
	var application = angular.module("application"),
		usersPath = '/api/users';

	// User Service
	function UserService($http) {
		 
		// A login function
		this.loginUser = function(credentials) {
			// The header 'X-Requested-With' is added to prevent the browser security popup
			var headers = { 
				authorization : "Basic " + btoa(credentials.username + ":" + credentials.password),
				'X-Requested-With' : 'XMLHttpRequest'
			};
	    
	    	return $http.get(usersPath + '/currentuser', {headers : headers});
		};
		
		// A logout function
		this.logoutUser = function() {
			return $http.post('/logout');
		};

		// Add or Update User
		this.addUpdateUser = function(data) {
			/*
			 * Check if user's id is null then make a post request, otherwise
			 * make a put request
			 */
			if (data.id === undefined || data.id === null) {
				return $http.post(usersPath, data);
			} else {
				return $http.put(usersPath, data);
			}
		};
		
		// Get User By Id
		this.getById = function(userId) {
			var path = usersPath + '/' + userId;
			return $http.get(path);
		};
		
		// Remove User
		this.deleteUser = function(data) {
			var path = usersPath + '?id=' + data.id;
			return $http.delete(path);
		};

		// Get All Users
		this.getAllUsers = function() {
			return $http.get(usersPath);
		};
		
		// Set target User, when row is selected
		var targetUser = {};
		this.setTargetUser = function(user){
			targetUser = user;
		};
		
		// Get target User, to be displayed on the add/update page
		this.getTargetUser = function(){
			return targetUser;
		};
	}

	application.service("UserService", UserService);
})();