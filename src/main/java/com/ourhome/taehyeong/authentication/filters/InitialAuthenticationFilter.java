package com.ourhome.taehyeong.authentication.filters;

import com.ourhome.taehyeong.authentication.OtpAuthentication;
import com.ourhome.taehyeong.authentication.UsernamePasswordAuthentication;
import com.ourhome.taehyeong.authentication.provider.OtpAuthenticationProvider;
import com.ourhome.taehyeong.authentication.provider.UsernamePasswordAuthenticationProvider;
import com.ourhome.taehyeong.entities.User;
import com.ourhome.taehyeong.entities.enums.Role;
import com.ourhome.taehyeong.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Component
public class InitialAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private OtpAuthenticationProvider otpAuthenticationProvider;

    @Autowired
    private UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String code = request.getHeader("code");

        if (code == null) {
            Authentication a = new UsernamePasswordAuthentication(username, password);
            usernamePasswordAuthenticationProvider.authenticate(a);
        } else {
            Authentication a = new OtpAuthentication(username, code);
            otpAuthenticationProvider.authenticate(a);

            SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
            Optional<User> user = userRepository.findUserByUsername(username);
            Role role;
            if (user.isPresent())
                role = user.get().getRole();
            else role = Role.ROLE_EMPLOYEE;

            String jwt = Jwts.builder()
                    .setClaims(Map.of("username", username, "role", role.toString()))
                    .signWith(key)
                    .compact();
            response.setHeader("Authorization", jwt);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login") || request.getServletPath().equals("/user/add");
    }
}