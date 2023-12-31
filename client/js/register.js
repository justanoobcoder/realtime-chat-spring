const url = 'http://localhost:8080';

$(function() {
	// set up event listeners
	$('#login').click(() => {
		$('#login-card').attr('hidden', false);
		$('#register-card').attr('hidden', true);
	});
	$('#signup').click(() => {
		$('#login-card').attr('hidden', true);
		$('#register-card').attr('hidden', false);
	});
	$('#register-btn').click(register);
});

function register() {
	// get data from form
	const fullName = $('#register-fullname').val();
	const username = $('#register-username').val();
	const password = $('#register-password').val();
	const avatarUrl = $('#register-avatar').val();

	const data = {
		fullName,
		username,
		password,
		avatarUrl,
	};

	// send register request to server
	$.ajax({
		url: url + '/api/auth/register',
		type: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		data: JSON.stringify(data),
		success: (data) => {
			alert('Register success');
		},
		error: function(err) {
			alert('Register fail');
		},
	});
}