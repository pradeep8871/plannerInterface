angular.module('myApp', []).controller('namesCtrl', function($scope,$http) {
    $scope.firstName = "pradeep";

    $scope.unplanned = {};
    $scope.task = {};
    $scope.employees = [];
    $scope.showSecond = false;

    $scope.submitXml = function(){
        var task = $scope.task;
        $http({method:'POST',url: 'http://localhost:8081/api/opta/submitXml',data:task}).then(function(response) {
            $scope.employees = response.data.DATA.emplyees;
            $scope.tasksList = response.data.DATA.unassignTask;
            $scope.unavailableEmpl = response.data.DATA.unavailableEmployees;
            $scope.taskListSize = response.data.DATA.taskListSize;
            // $scope.citizenList = response.data.DATA.citizenList;
            $scope.vehicleList = response.data.DATA.vehicleList;
            $scope.employeeList = response.data.DATA.employeeList;
            $scope.unassigntasksList = response.data.DATA.unassignTaskList;
            $scope.assignedEmp = response.data.DATA.assignedEmp;
            $scope.unAssignEmp = response.data.DATA.unAssignEmp;
            //          $scope.problemIds = response.data.DATA;
            //            $scope.problemId = $scope.problemIds[0]; 
        });
    };

    $scope.getStatusClass = function(value){
        if(value) return "taskHardContraints";

    };

    $scope.getStatus = function(){
        $http({method:'GET',url: 'http://localhost:8081/api/opta/getStatus',params: {id: $scope.problemId.planningId}}).then(function(response) {
            $scope.planningProblem = response.data.DATA;
        });

    };

    $scope.getReasonByTask = function(value){
        if(value[0]!=0)return "Skill Mismatch";
        if(value[1]!=0)return "Wrong order";
        if(value[2]!=0)return "Dup Vehicle";
        if(value[3]!=0)return "Exceeds Availability";
        if(value[4]!=0)return "Exceeds Availability";
        if(value[5]!=0)return "Employee not assigned";
        if(value[6]!=0)return "Vehicle not assigned";
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
