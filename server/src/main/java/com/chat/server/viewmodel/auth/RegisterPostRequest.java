package com.chat.server.viewmodel.auth;

public record RegisterPostRequest(
        String username,
        String fullName,
        String password,
        String avatarUrl
) { }
