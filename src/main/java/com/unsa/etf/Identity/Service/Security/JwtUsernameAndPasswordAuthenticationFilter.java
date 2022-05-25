package com.unsa.etf.Identity.Service.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.Collections;
import java.util.stream.Collectors;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authManager;

    private final JwtConfig jwtConfig;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;

        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {

            //Get credentials from request (temporary class)
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);

            //Create auth object (contains credentials) which will be used by auth manager
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    creds.getUsername(), creds.getPassword(), Collections.emptyList());
            System.out.println("attempt");
            System.out.println(userDetailsService.loadUserByUsername(creds.getUsername()).getPassword());
            System.out.println(passwordEncoder.matches(creds.getPassword(), userDetailsService.loadUserByUsername(creds.getUsername()).getPassword()));
            //Authentication manager authenticate the user, and use UserDetialsServiceImpl::loadUserByUsername() method to load the user.
            //This internally calls passwordEncoder.matcher(passwordFromRequest (this is generally non hashed pw that user types in), hashedPasswordFromDB)
            return authManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Upon successful authentication, generate a token.
    // The 'auth' passed to successfulAuthentication() is the current authenticated user.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        Long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(auth.getName())
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();

        // Add token to header
        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
    }

    // A (temporary) class just to represent the user credentials
    private static class UserCredentials {
        private String username, password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
