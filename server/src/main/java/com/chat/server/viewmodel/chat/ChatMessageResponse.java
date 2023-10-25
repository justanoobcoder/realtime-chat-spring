package com.chat.server.viewmodel.chat;

import lombok.*;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {
    private String sender;
    private String avatarUrl;
    private String content;
    private String time;
}
