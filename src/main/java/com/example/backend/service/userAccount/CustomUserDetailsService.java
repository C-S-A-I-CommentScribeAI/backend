package com.example.backend.service.userAccount;

import com.example.backend.UserAccount.entity.UserAccount;
import com.example.backend.UserAccount.repository.UserAccountRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUserId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(UserAccount userAccount) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userAccount.getUserRole().toString());

        return new User(
                String.valueOf(userAccount.getId()),
                userAccount.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
