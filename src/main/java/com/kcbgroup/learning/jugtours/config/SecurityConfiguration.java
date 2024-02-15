package com.kcbgroup.learning.jugtours.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import com.kcbgroup.learning.jugtours.web.CookieCsrfFilter;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Value("${okta.oauth2.okta-oauth2-issuer}")
    private String issuerUri;
    @Value("${okta.oauth2.okta-oauth2-client-id}")
    private String clientId;
    @Value("${okta.oauth2.okta-oauth2-client-secret}")
    private String clientSecret;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/", "/index.html", "*.ico", "*.css", "*.js", "/api/user").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(withDefaults())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()))
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    ClientRegistration clientRegistration() {
        return clientRegistrationBuilder().build();
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository(ClientRegistration clientRegistration) {
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

    private ClientRegistration.Builder clientRegistrationBuilder() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("end_session_endpoint", "http://localhost:8080/api/logout");

        return ClientRegistration.withRegistrationId("okta")
                .issuerUri(issuerUri)
                .redirectUri("http://localhost:4200/login/oauth2/code/okta")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("read:user")
                .authorizationUri(issuerUri + "/login/oauth/authorize")
                .tokenUri(issuerUri + "/login/oauth/access_token")
                .jwkSetUri(issuerUri + "/oauth/jwk")
                .userInfoUri("http://localhost:8080/api/user")
                .providerConfigurationMetadata(metadata)
                .userNameAttributeName("id")
                .clientName("Baraza Spring Blog")
                .clientId(clientId)
                .clientSecret(clientSecret);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return mock(JwtDecoder.class);
    }

    @Bean
    OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }
}
