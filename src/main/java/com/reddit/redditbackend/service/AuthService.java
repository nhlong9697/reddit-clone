package com.reddit.redditbackend.service;

import com.reddit.redditbackend.dto.AuthenticationResponse;
import com.reddit.redditbackend.dto.LoginRequest;
import com.reddit.redditbackend.dto.RefreshTokenRequest;
import com.reddit.redditbackend.dto.RegisterRequest;
import com.reddit.redditbackend.exception.SpringRedditException;
import com.reddit.redditbackend.model.AppUser;
import com.reddit.redditbackend.model.NotificationEmail;
import com.reddit.redditbackend.model.VerificationToken;
import com.reddit.redditbackend.repository.UserRepository;
import com.reddit.redditbackend.repository.VerificationTokenRepository;
import com.reddit.redditbackend.security.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RefreshTokenService refreshTokenService;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, MailService mailService, AuthenticationManager authenticationManager, JwtProvider jwtProvider, VerificationTokenRepository verificationTokenRepository1, RefreshTokenService refreshTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.verificationTokenRepository = verificationTokenRepository1;
        this.refreshTokenService = refreshTokenService;
    }
    @Value("${backend.api}")
    private String BACKEND_API;
    @Transactional
    public void signup(RegisterRequest registerRequest) {
        AppUser appUser = new AppUser();
        appUser.setUsername(registerRequest.getUsername());
        appUser.setEmail(registerRequest.getEmail());
        appUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        appUser.setCreated(Instant.now());
        appUser.setEnabled(false);

        userRepository.save(appUser);

        String token = generateVerificationToken(appUser);
        mailService.sendMail(new NotificationEmail("Please Activate your account",
                appUser.getEmail(), "Thank you for signing up, please click on the below url to " +
                "active your account : "
                + BACKEND_API
                + "api/auth/accountVerification/"
                + token));
    }

    private String generateVerificationToken(AppUser appUser) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setAppUser(appUser);
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
        String username = verificationToken.getAppUser().getUsername();
        AppUser appUser =
                userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException(
                "AppUser not found with name - " + username));
        appUser.setEnabled(true);
        userRepository.save(appUser);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticatingObject =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticatingObject);
        String token = jwtProvider.generateToken(authenticatingObject);

        String refreshToken = refreshTokenService.generateRefreshToken().getToken();
        Instant expiresAt = Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis());
        String userName = loginRequest.getUsername();
        return new AuthenticationResponse(token, refreshToken,expiresAt, userName);
    }
    @Transactional(readOnly = true)
    public AppUser getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = 
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("AppUser name not found - " + principal.getUsername()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String authenticationToken =
                jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAuthenticationToken(authenticationToken);
        authenticationResponse.setRefreshToken(refreshTokenRequest.getRefreshToken());
        authenticationResponse.setExpiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()));
        authenticationResponse.setUsername(refreshTokenRequest.getUsername());
        return authenticationResponse;
    }
}
