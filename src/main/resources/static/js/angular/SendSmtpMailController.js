angular.module('smtpMailer', []).controller('SendSmtpMailController', function($scope, $http) {
	
	$http.get('/getSmtpMailBean').then(function(response) {
		$scope.smtpMailContent = response.data;
		console.log("hello", $scope.smtpMailContent);
	}, function() {
		alert('error');
	});
	
	$scope.send = function() {
		var data = {
			username: $scope.smtpMailContent.username,
			password: $scope.smtpMailContent.password,
			fromAddress: $scope.smtpMailContent.fromAddress,
			toAddress: $scope.smtpMailContent.toAddress,
			subject: $scope.smtpMailContent.subject,
			plainContent: $scope.smtpMailContent.plainContent,
			htmlContent: $scope.smtpMailContent.htmlContent
		};
		
		$http.post('/sendSmtpMail', data).then(function() {
			alert('success');
		}, function() {
			alert('error');
		});
	};
});