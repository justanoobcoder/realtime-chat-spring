$(function() {
    const stompClient = new StompJs.Client({
        brokerURL: 'ws://localhost:8080/websocket',
        reconnectDelay: 5000,
        connectHeaders: {
            'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoaWVwbmgiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjk4MjE2ODA2LCJleHAiOjE2OTgzMDMyMDZ9.TL-U0G1zvF9AWgeVwjwYd5Daww8chA0IaHfHrBIVIeY',
            login: '',
            passcode: ''
        },
    });

    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public', (payload) => {
            console.log(payload);
            const data = JSON.parse(payload.body);
            console.log(data);
            $('#chat-list').append('<li>' + data.sender + ': ' + data.content + '</li>');
        });
    };
    
    stompClient.onWebSocketClose = function() {
        console.log('Connection closed');
    };

    stompClient.activate();

    $('#send-btn').click(() => {
        const username = $('#username').val();
        const message = $('#message').val();
        const payload = { sender: username, content: message };

        stompClient.publish({ destination: '/app/chat/public', body: JSON.stringify(payload) });
    });
});