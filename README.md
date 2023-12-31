# Real-time chat application
This is a simple real-time chat application using Spring Boot Websocket

## Screenshots
![Pic1](https://raw.githubusercontent.com/justanoobcoder/realtime-chat-spring/main/screenshots/screenshot-01.png)

## Technologies
- Server: Spring Boot, WebSocket with Stomp
- Client: HTML, JavaScript, JQuery, AJAX, StompJS
- Database: In-memory H2 database

## Features
- Register
- Login
- Real-time chat

## How to use
### With Docker
Simply run command `docker compose up -d`, then open browser with url `http://127.0.0.1:5500`, open more browsers to test the chat app.

Run `docker compose down` to stop the app.

### Without Docker
**Start server**: go to `server/` folder, run command `mvnw spring-boot:run` to start the server.

**Start client**: go to `client/` folder, open this folder using Visual Studio Code with [Live Server](https://marketplace.visualstudio.com/items?itemName=ritwickdey.LiveServer) extension.

Open brower with url `http://127.0.0.1:5500`, open more browsers to test the chat app.

## To do
- [ ] Chat one-to-one
- [ ] Create chat group, invite user to group
- [ ] Use database to save messages
