// set up jquery
$(function () {
	// check if user is logged in
	if (token && currentUser) {
		$('#login-card').attr('hidden', true);
		$('#chat-area').attr('hidden', false);
		connectToChat(token);
	}

	// set up event listeners
	$('#login-btn').click(login);
	$('#login-password').keypress((e) => {
		if (e.which == 13) {
			login();
			return false;
		}
	});
	$('#logout-btn').click(logout);
	$('#send-btn').click(sendMessage);
	$('#message').keypress((e) => {
		if (e.which == 13) {
			sendMessage();
			return false;
		}
	});

});

// functions
function login() {
	const username = $('#login-username').val();
	const password = $('#login-password').val();

	// send login request to server
	$.ajax({
		url: url + loginPath,
		type: 'POST',
		headers: {
			'Content-Type': 'application/json',
			'Accept': 'application/json'
		},
		data: JSON.stringify({ username, password }),
		success: (data) => {
			console.log(data);

			// save token and current user to local storage
			localStorage.setItem('token', data.accessToken);
			localStorage.setItem('currentUser', JSON.stringify(data.account));

			// hide login card and show chat area
			$('#login-card').attr('hidden', true);
			$('#chat-area').attr('hidden', false);
			$('#login-username').val('');
			$('#login-password').val('');

			connectToChat(data.accessToken);
		},
		error: (err) => {
			alert('Login fail');
		}
	});
}

function logout() {
	// disconnect from websocket, remove token and current user from local storage
	stompClient.deactivate();

	let accessToken = localStorage.getItem('token');
	$.ajax({
		url: url + logoutPath,
		type: 'POST',
		headers: {
			'Authorization': 'Bearer ' + accessToken,
		},
		success: (data) => {
			localStorage.removeItem('token');
			localStorage.removeItem('currentUser');
		
			// hide chat area and show login card
			$('#login-card').attr('hidden', false);
			$('#chat-area').attr('hidden', true);
			$('#message').val('');
		},
	});
}