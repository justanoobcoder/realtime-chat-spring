package com.chat.server.model.view.auth;

public record RegisterPostRequest(
        String username,
        String fullName,
        String password,
        String avatarUrl) {
}
