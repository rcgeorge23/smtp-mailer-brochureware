angular.module('smtpMailer', []).controller('SendSmtpMailController', function($scope, $http) {
	
	$http.get('/getConfiguration').then(function(response) {
		$scope.smtpConfiguration = response.data;
	}, function() {
		alert('error');
	});
	
	$http.get('/getSmtpMailBean').then(function(response) {
		$scope.smtpMailContent = response.data;
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
	
	$scope.getMessage = function() {
		$http.get('http://' + $scope.smtpConfiguration.fakeSmtpHost, {params: {
			username: $scope.smtpMailContent.username,
			password: $scope.smtpMailContent.password,
			toAddress: $scope.smtpMailContent.toAddress}}).then(function(response) {
				console.log('response', response);
				$scope.jsonMessage = response.data;
			}, function() {
				alert('error');
			}
		);
	};
});