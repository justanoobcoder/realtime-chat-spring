package com.chat.server.web.websocket;

import com.chat.server.service.AccountService;
import com.chat.server.viewmodel.chat.ChatMessageRequest;
import com.chat.server.viewmodel.chat.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Controller
@MessageMapping("/chat")
public class ChatController {
    private final AccountService accountService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @MessageMapping("/public")
    public void publicChat(@Payload ChatMessageRequest message, Principal principal) {
        var account = accountService.getByUsername(principal.getName());
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String time = date.format(formatter);
        var response = ChatMessageResponse.builder()
            .sender(account.getFullName())
            .avatarUrl(account.getAvatarUrl())
            .content(HtmlUtils.htmlEscape(message.getContent()))
            .time(time)
            .build();

        List<String> subscribers = simpUserRegistry.getUsers().stream()
            .map(SimpUser::getName)
            .filter(name -> !name.equals(principal.getName()))
            .toList();
        subscribers
            .forEach(s -> simpMessagingTemplate
                .convertAndSendToUser(s, "/queue/chat/public", response)
            );
    }
}
