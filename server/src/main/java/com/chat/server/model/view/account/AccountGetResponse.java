package com.chat.server.model.view.account;

public record AccountGetResponse(
        Long id,
        String username,
        String fullName,
        String avatarUrl) {
}
