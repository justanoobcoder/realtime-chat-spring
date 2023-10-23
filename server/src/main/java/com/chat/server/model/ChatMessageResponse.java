package com.chat.server.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageResponse {
    private String sender;
    private String content;
}
