package com.chat.server.model.view.auth;

public record LoginPostRequest(
        String username,
        String password) {
}
