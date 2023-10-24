package com.chat.server.viewmodel;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageRequest {
    private String sender;
    private String content;
}
