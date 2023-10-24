package com.chat.server.viewmodel;

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
