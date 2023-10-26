// set up
const socketUrl = 'ws://localhost:8080/websocket';
const publicChatTopicPath = '/user/queue/chat/public';
const publicChatPath = '/app/chat/public';
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
