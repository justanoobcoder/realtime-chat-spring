package com.chat.server.controller;

import com.chat.server.model.ChatMessageRequest;
import com.chat.server.model.ChatMessageResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/chat")
public class ChatController {
    @MessageMapping("/public")
    @SendTo("/topic/public")
    public ChatMessageResponse publicChat(@Payload ChatMessageRequest message) {
        return ChatMessageResponse.builder()
                .sender(message.getSender())
                .content(message.getContent())
                .build();
    }
}
