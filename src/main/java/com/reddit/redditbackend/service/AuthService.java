package com.reddit.redditbackend.service;

import com.reddit.redditbackend.dto.AuthenticationResponse;
import com.reddit.redditbackend.dto.LoginRequest;
import com.reddit.redditbackend.dto.RegisterRequest;
import com.reddit.redditbackend.exception.SpringRedditException;
import com.reddit.redditbackend.model.NotificationEmail;
import com.reddit.redditbackend.model.User;
import com.reddit.redditbackend.model.VerificationToken;
import com.reddit.redditbackend.repository.UserRepository;
import com.reddit.redditbackend.repository.VerificationTokenRepository;
import com.reddit.redditbackend.security.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, MailService mailService, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your account",
                user.getEmail(), "Thank you for signing up, please click on the below url to " +
                "active your account : " + "http://localhost:8080/api/auth/accountVerification/"+token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken =
                verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        enableUserByToken(verificationToken.get());
    }

    private void enableUserByToken(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user =
                userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException(
                "User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticatingObject =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticatingObject);
        String token = jwtProvider.generateToken(authenticatingObject);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
