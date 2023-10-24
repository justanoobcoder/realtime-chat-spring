package com.chat.server.controller;

import com.chat.server.service.AuthenticationService;
import com.chat.server.viewmodel.auth.RegisterPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterPostRequest request) {
        authenticationService.register(request);
        return ResponseEntity.ok().build();
    }
}
