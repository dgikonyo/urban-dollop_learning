package com.kcbgroup.learning.jugtours.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

import static java.util.Map.of;

@RestController
public class UserController {
    private final ClientRegistration registration;

    public UserController(ClientRegistrationRepository registrationRepository) {
        this.registration = registrationRepository.findByRegistrationId("okta");
    }

    @GetMapping("/api/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            return ResponseEntity.ok().body(user.getAttributes());
        }
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // send logout URL to client so that they can initiate logout
        var issueUri = registration.getProviderDetails().getIssuerUri();
        var originalUrl = request.getHeader(HttpHeaders.ORIGIN);
        Object[] params = {issueUri, registration.getClientId(), originalUrl};

        var logoutUrl = MessageFormat.format("{0}v2/logout?client_id={1}&returnTo={2}", params);
        request.getSession().invalidate();

        return ResponseEntity.ok().body(of("logoutUrl", logoutUrl));
    }
}
