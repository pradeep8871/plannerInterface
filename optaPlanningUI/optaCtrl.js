angular.module('myApp', []).controller('namesCtrl', function($scope,$http) {
    $scope.firstName = "pradeep";
    
    $scope.unplanned = {};
    $scope.task = {};
    $scope.employees = [];
    $scope.showSecond = false;
    
    $scope.submitXml = function(){
        var task = $scope.task;
        $http({method:'POST',url: 'http://localhost:8081/api/opta/submitXml',data:task}).then(function(response) {
            $scope.problemIds = response.data.DATA;
            $scope.problemId = $scope.problemIds[0]; 
        });
    };
    
    $scope.getStatus = function(){
        $http({method:'GET',url: 'http://localhost:8081/api/opta/getStatus',params: {id: $scope.problemId.planningId}}).then(function(response) {
            $scope.planningProblem = response.data.DATA;
        });
        
    };
    
    $scope.getSolution = function(){
        $http({method:'GET',url: 'http://localhost:8081/api/opta/getSolution',params: {id:$scope.problemId.planningId}}).then(function(response) {
            $scope.employees = response.data.DATA;
        });
    };
    
    $scope.getProblemIds = function(){
        $http({method:'GET',url: 'http://localhost:8081/api/opta/getProblemIds'}).then(function(response) {
            $scope.problemIds = response.data.DATA;
            $scope.problemId = $scope.problemIds[0];
        });
    };
    $scope.getProblemIds();

     $scope.makeLocationData = function(){
        $http({method:'POST',url: 'http://localhost:8081/api/opta/makeLocationData'}).then(function(response) {
        });
        
    };
/*        
    $scope.deleteData = function(){
        $http({method:'POST',url: 'http://localhost:8080/api/opta'}).then(function(response) {
            $scope.greeting = response.data;
        });
        
    };
    
    $scope.getPlannedSolution = function(){
        $http({method:'POST',url: 'http://localhost:8080/api/opta'}).then(function(response) {
            $scope.greeting = response.data;
        });
        
    };
    
    
    */
    
    
}).config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);
