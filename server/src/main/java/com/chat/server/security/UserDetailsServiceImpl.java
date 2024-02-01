package com.chat.server.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chat.server.model.entity.Account;
import com.chat.server.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Get the account from the database and create a UserDetails object from it.
        // The UserDetails object is used by Spring Security to authenticate the user.
        var account = accountRepository.findByUsername(username);
        return account.map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private UserDetails createUserDetails(Account account) {
        var authorities = List.of((GrantedAuthority) () -> "ROLE_USER");
        return new User(account.getUsername(), account.getPassword(), authorities);
    }
}
