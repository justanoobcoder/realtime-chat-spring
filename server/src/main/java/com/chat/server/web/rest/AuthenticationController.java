package com.chat.server.web.rest;

import com.chat.server.service.AccountService;
import com.chat.server.service.AuthenticationService;
import com.chat.server.viewmodel.account.AccountGetResponse;
import com.chat.server.viewmodel.auth.LoginPostRequest;
import com.chat.server.viewmodel.auth.LoginPostResponse;
import com.chat.server.viewmodel.auth.RegisterPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final AccountService accountService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterPostRequest request) {
        authenticationService.register(request);
        var account = new AccountGetResponse(null, request.username(), request.fullName(), request.avatarUrl());
        // Notify all users that a new user has been registered
        simpMessagingTemplate.convertAndSend("/topic/users/new-user", account);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginPostResponse> login(@RequestBody LoginPostRequest request) {
        LoginPostResponse response = authenticationService.login(request);
        // Notify all users that a user has been logged in
        simpMessagingTemplate.convertAndSend("/topic/users/login", response.account());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Principal principal) {
        var account = accountService.getByUsername(principal.getName());
        var response = new AccountGetResponse(account.getId(), account.getUsername(),
            account.getFullName(), account.getAvatarUrl());
        // Notify all users that a user has been logged out
        simpMessagingTemplate.convertAndSend("/topic/users/logout", response);
        return ResponseEntity.ok().build();
    }
}
