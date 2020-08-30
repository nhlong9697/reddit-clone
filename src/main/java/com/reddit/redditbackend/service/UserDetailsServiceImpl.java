package com.reddit.redditbackend.service;

import com.reddit.redditbackend.model.AppUser;
import com.reddit.redditbackend.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional(readOnly = true)
    //load user framework type by user name
    public UserDetails loadUserByUsername(String username) {
        Optional<AppUser> userOptional = userRepository.findByUsername(username);
        AppUser appUser = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No appUser " +
                        "Found with username : " + username));

        return new org.springframework.security
                .core.userdetails.User(appUser.getUsername(), appUser.getPassword(),
                appUser.isEnabled(), true, true,
                true, getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }
}
