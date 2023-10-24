package com.chat.server.service;

import com.chat.server.entity.Account;
import com.chat.server.repository.AccountRepository;
import com.chat.server.viewmodel.auth.RegisterPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final AccountRepository accountRepository;

    public void register(RegisterPostRequest request) {
        Account account = Account.builder()
                .username(request.username())
                .fullName(request.fullName())
                .password(request.password())
                .avatarUrl(request.avatarUrl())
                .build();
        accountRepository.save(account);
    }
}
