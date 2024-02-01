package com.chat.server.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chat.server.exception.DuplicatedException;
import com.chat.server.model.entity.Account;
import com.chat.server.model.view.account.AccountGetResponse;
import com.chat.server.model.view.auth.LoginPostRequest;
import com.chat.server.model.view.auth.LoginPostResponse;
import com.chat.server.model.view.auth.RegisterPostRequest;
import com.chat.server.repository.AccountRepository;
import com.chat.server.security.jwt.JwtService;
import com.chat.server.util.Constants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterPostRequest request) {
        if (accountRepository.existsByUsername(request.username())) {
            throw new DuplicatedException(Constants.USERNAME_ALREADY_EXISTS, request.username());
        }
        Account account = Account.builder()
                .username(request.username())
                .fullName(request.fullName())
                .password(passwordEncoder.encode(request.password()))
                .avatarUrl(request.avatarUrl())
                .build();
        accountRepository.save(account);
    }

    public LoginPostResponse login(LoginPostRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String accessToken = jwtService.createToken(authentication);
        var account = accountRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + request.username()));
        var accountResponse = new AccountGetResponse(
                account.getId(),
                account.getUsername(),
                account.getFullName(),
                account.getAvatarUrl());
        return new LoginPostResponse(accessToken, accountResponse);
    }
}
