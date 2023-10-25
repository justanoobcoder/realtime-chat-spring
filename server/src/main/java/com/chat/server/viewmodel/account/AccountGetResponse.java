package com.chat.server.viewmodel.account;

public record AccountGetResponse(
        Long id,
        String username,
        String fullName,
        String avatarUrl
) {
}
