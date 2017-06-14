angular.module('smtpMailer', []).controller('SendSmtpMailController', function($scope, $http) {
	$scope.smtpMailContent = {
		username: "my-username",
		password: "my-password",
		fromAddress: "sender@email.com",
		toAddress: "recipient@email.com",
		subject: "Really Important Email",
		plainContent: "This is a really important message!",
		htmlContent: "This is a <strong>really important</strong> message!"
	};
	
	console.debug("hello", $scope.smtpMailContent);
	
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