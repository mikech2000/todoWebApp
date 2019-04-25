(function() {
	var app = angular.module('todoApp', []);

	app.controller('JsonController', function($scope, $http) {
		this.todoItems = null;
		this.serverInfo = 'uninitialized';
		this.hideCompleted = true;
		
		this.getItems = function() {
			var hc = $scope.hideCompleted;
			$http.get("/todoWebApp/rs/todoApp/getAllItems?hideCompleted="+hc)
		    .success(function (response) {
		    	$scope.todoItems = response;
		    	});
		}
		
		this.hideCompleted = function(item) {
			this.getItems();
		}
		
		this.serverInfo = function(item) {
			$http.get("/todoWebApp/rs/todoApp/getServerInfo")
		    .success(function (response) {
		    	$scope.serverInfo = response;
		    	});
		}
		
		this.getItems();
	});
	
	app.filter('myDateFormat', function myDateFormat($filter) {
		return function(text) {
			var  tempdate= new Date(text.replace(/-/g,"/"));
		    return $filter('date')(tempdate, "MMM-dd-yy @ HH:mm");
		}
	});

	app.controller('FormController', function($scope, $http) {
		this.newItem = {};
		this.doSubmit = function() {
			$http.post("/todoWebApp/rs/todoApp/addItem", this.newItem)
		    .success(function (response) {
		    	$scope.jsonCtrl.getItems();
		    	});
			this.newItem = {};
		}
	});
	
	app.controller('TableController', function($scope, $http){
		this.change = function(itemNum) {
			$http.put("/todoWebApp/rs/todoApp/completedChanged", itemNum)
		    .success(function (response) {
		    	$scope.jsonCtrl.getItems();
		    	});
		}		
	});
	
})();
