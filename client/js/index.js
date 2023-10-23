$(function() {
    const stompClient = new StompJs.Client({
        brokerURL: 'ws://localhost:8080/websocket',
        reconnectDelay: 5000,
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