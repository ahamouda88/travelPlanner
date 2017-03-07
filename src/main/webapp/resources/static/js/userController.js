(function() {
	var application = angular.module("application");

	// User Controller
	function UserController($scope, $rootScope, UserService, PagerService) {
		// Add or Update User
		$scope.addUpdateUser = function() {
			$scope.errorMessage = null;
			var dataObj = {
				id : $scope.user.id,
				firstName : $scope.user.firstName,
				lastName : $scope.user.lastName,
				username : $scope.user.username,
				password : $scope.user.password,
				confirmPassword : $scope.user.confirmPassword,
				role : $scope.user.role ? $scope.user.role : 'REGULAR_USER',
				version : $scope.user.version
			};
			
			// Verify if it is an insert or update request
			$scope.insert = $scope.user.id === undefined ? true : false;

			// Password and confirm password validation
			if ($scope.user.confirmPassword !== $scope.user.password) {
				$scope.user.response = {
					errorCode : -1,
					message : 'Password and Confirm password should match!'
				};
				clearPassword();
			} else {
				var response = UserService.addUpdateUser(dataObj);
				response.success(function(data) {
					$scope.user = {};
					$scope.user.response = data;
				});
				response.error(function(data) {
					$scope.user.response = data;
					clearPassword();
				});
			}
		};

		// Delete User
		$scope.deleteUser = function(user) {
			if (confirm('Are you sure you want to delete this user: '
					+ user.username + ' ?')) {
				var response = UserService.deleteUser(user).then(function() {
					$scope.users = $scope.getAllUsers();
					$scope.deletedUser = user;
				});
			}
		};

		// Set target User, when row is selected
		$scope.setTargetUser = function(user) {
			user.confirmPassword = user.password;
			UserService.setTargetUser(user);
		};

		// Get target User, to be displayed on the add/update page
		$scope.getTargetUser = function() {
			var emptyUser = {};
			$scope.user = UserService.getTargetUser();
			UserService.setTargetUser(emptyUser);
		};

		// Get All Users
		$scope.getAllUsers = function() {
			var response = UserService.getAllUsers();
			response.success(function(data) {
				$scope.users = data;
				setUpPagination(data);
			});
		};
		
		$scope.setNext = function() {
			$scope.currentPage = $scope.tablePagination.setNext();
		};
		
		$scope.setPrev = function() {
			$scope.currentPage = $scope.tablePagination.setPrev();
		}
		
		// Setting up pagination
		function setUpPagination(data){
			$scope.tablePagination = PagerService.tablePagination(data, 6);
			$scope.currentPage = $scope.tablePagination.currentPage;
		}
		
		function clearPassword(){
			$scope.user.password = '';
			$scope.user.confirmPassword = '';
		}
	}

	application.controller('UserController', UserController);
})();