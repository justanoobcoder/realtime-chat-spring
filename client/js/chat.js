// set up
const socketUrl = 'ws://localhost:8080/websocket';
const publicNewUserTopicPath = '/topic/users/new-user';
const publicUserLoginTopicPath = '/topic/users/login';
const publicUserLogoutTopicPath = '/topic/users/logout';
const publicChatTopicPath = '/user/queue/chat/public';
const publicChatPath = '/app/chat/public';
const loginPath = '/api/auth/login';
const logoutPath = '/api/auth/logout';
const token = localStorage.getItem('token');
const currentUser = JSON.parse(localStorage.getItem('currentUser'));

// set up stomp client
const stompClient = new StompJs.Client({
	brokerURL: socketUrl,
	reconnectDelay: 5000,
});

stompClient.onConnect = (frame) => {
	console.log('Connected: ' + frame);
	stompClient.subscribe(publicNewUserTopicPath, newUserSubscribe);
	stompClient.subscribe(publicUserLoginTopicPath, userLoginSubscribe);
	stompClient.subscribe(publicUserLogoutTopicPath, userLogoutSubscribe);
	stompClient.subscribe(publicChatTopicPath, publicChatSubscribe);
};

stompClient.onStompError = (frame) => {
	console.log('Error: ' + frame);
	logout();
}

stompClient.onWebSocketClose = () => {
	console.log('Connection closed');
};

$(function () {
	$('#action_menu_btn').click(() => $('.action_menu').toggle());
});

function connectToChat(accessToken) {
	$('#msg-chat-box').scrollTop($('#msg-chat-box')[0].scrollHeight);
	// set authorization header and connect to websocket
	stompClient.connectHeaders = {
		Authorization: 'Bearer ' + accessToken,
	};
	stompClient.activate();
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

	$('#public-short-msg').text(message);
}

function newUserSubscribe(payload) {
	const data = JSON.parse(payload.body);
	let newContactElement =
		`<li>
			<div class="d-flex bd-highlight">
				<div class="img_cont">
					<img
						src="${data.avatarUrl}"
						class="rounded-circle user_img">
					<span class="online_icon offline" id="${data.username}"></span>
				</div>
				<div class="user_info">
					<span>${data.fullName}</span>
					<div class="short-msg">
						<p></p>
					</div>
				</div>
			</div>
		</li>`;
	$('.contacts').append(newContactElement);
	let notifyMsg = 
		`<div class="d-flex justify-content-center mt-2 mb-2">
			<div class="notify-msg">
				${data.fullName} has joined the group!
			</div>
	  </div>`;
	$('.msg_card_body').append(notifyMsg);
	$('#msg-chat-box').scrollTop($('#msg-chat-box')[0].scrollHeight);
}

function userLoginSubscribe(payload) {
	const data = JSON.parse(payload.body);
	$('#' + data.username).removeClass('offline');
	let notifyMsg = 
		`<div class="d-flex justify-content-center mt-2 mb-2">
			<div class="notify-msg">
				${data.fullName} is online!
			</div>
	  </div>`;
	$('.msg_card_body').append(notifyMsg);
	$('#msg-chat-box').scrollTop($('#msg-chat-box')[0].scrollHeight);
}

function userLogoutSubscribe(payload) {
	const data = JSON.parse(payload.body);
	$('#' + data.username).addClass('offline');
	let notifyMsg = 
		`<div class="d-flex justify-content-center mt-2 mb-2">
			<div class="notify-msg">
				${data.fullName} is offline!
			</div>
	  </div>`;
	$('.msg_card_body').append(notifyMsg);
	$('#msg-chat-box').scrollTop($('#msg-chat-box')[0].scrollHeight);
}

function publicChatSubscribe(payload) {
	const data = JSON.parse(payload.body);
	let otherMsgElement =
		`<div class="d-flex justify-content-start mb-4">
			<div class="img_cont_msg">
				<img src="${data.avatarUrl}"
					class="rounded-circle user_img_msg">
			</div>
			<div class="msg_container">
				<span class="msg-sender-name">${data.sender}</span>
				${data.content}
				<span class="msg_time">8:40 AM, Today</span>
			</div>
		</div>`;
	$('.msg_card_body').append(otherMsgElement);
	$('#msg-chat-box').scrollTop($('#msg-chat-box')[0].scrollHeight);

	$('#public-short-msg').text(data.content);
}
