// set up
const socketUrl = 'ws://localhost:8080/websocket';
const publicChatTopicPath = '/user/queue/chat/public';
const publicChatPath = '/app/chat/public';
const url = 'http://localhost:8080';
const loginPath = '/api/auth/login';
const token = localStorage.getItem('token');
const currentUser = JSON.parse(localStorage.getItem('currentUser'));

// set up stomp client
const stompClient = new StompJs.Client({
	brokerURL: socketUrl,
	reconnectDelay: 5000,
});

stompClient.onConnect = (frame) => {
	console.log('Connected: ' + frame);
	stompClient.subscribe(publicChatTopicPath, publicSubscribe);
};

stompClient.onWebSocketClose = function () {
	console.log('Connection closed');
};

// set up jquery
$(function () {
	// check if user is logged in
	if (token && currentUser) {
		$('#card').attr('hidden', true);
		$('#chat-area').attr('hidden', false);
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
			$('#card').attr('hidden', true);
			$('#chat-area').attr('hidden', false);
			$('#msg-chat-box').scrollTop($('#msg-chat-box')[0].scrollHeight);
			$('#login-username').val('');
			$('#login-password').val('');

			// set authorization header and connect to websocket
			stompClient.connectHeaders = {
				Authorization: 'Bearer ' + data.accessToken,
			};
			stompClient.activate();

		},
		error: (err) => {
			console.log(err);
		}
	});
}

function logout() {
	// disconnect from websocket, remove token and current user from local storage
	stompClient.deactivate();
	localStorage.removeItem('token');
	localStorage.removeItem('currentUser');

	// hide chat area and show login card
	$('#card').attr('hidden', false);
	$('#chat-area').attr('hidden', true);
	$('#message').val('');
}

function sendMessage() {
	const message = $('#message').val();
	if (!message) return;
	const payload = { content: message };
	const user = JSON.parse(localStorage.getItem('currentUser'));

	// send message to server
	stompClient.publish({ destination: publicChatPath, body: JSON.stringify(payload) });
	let myMsgElement = 
	`<div class="d-flex justify-content-end mb-4">
			<div class="msg_container my-msg">
				${message}
				<span class="msg_time_send">08:56 PM, Today</span>
			</div>
			<div class="img_cont_msg">
				<img src="${user.avatarUrl}"
					class="rounded-circle user_img_msg">
			</div>
		</div>`;
	$('.msg_card_body').append(myMsgElement);
	$('#message').val('');
	$('#msg-chat-box').scrollTop($('#msg-chat-box')[0].scrollHeight);
}

function publicSubscribe(payload) {
	console.log(payload);
	const data = JSON.parse(payload.body);
	console.log(data);
	let otherMsgElement = 
	`<div class="d-flex justify-content-start mb-4">
	<div class="img_cont_msg">
		<img src="${data.avatarUrl}"
			class="rounded-circle user_img_msg">
	</div>
	<div class="msg_container">
		${data.content}
		<span class="msg_time">8:40 AM, Today</span>
	</div>
</div>`;
	$('.msg_card_body').append(otherMsgElement);
	$('#msg-chat-box').scrollTop($('#msg-chat-box')[0].scrollHeight);
}
