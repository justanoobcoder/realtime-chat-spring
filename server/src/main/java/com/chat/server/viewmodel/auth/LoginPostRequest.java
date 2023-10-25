package com.chat.server.viewmodel.auth;

public record LoginPostRequest(
        String username,
        String password
) {
}
