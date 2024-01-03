package com.ourhome.taehyeong.config;

import com.ourhome.taehyeong.authentication.filters.InitialAuthenticationFilter;
import com.ourhome.taehyeong.authentication.filters.JwtAuthenticationFilter;
import com.ourhome.taehyeong.authentication.provider.OtpAuthenticationProvider;
import com.ourhome.taehyeong.authentication.provider.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private InitialAuthenticationFilter initialAuthenticationFilter;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/user/add")
                                    .permitAll()
                                .requestMatchers("/user/privacy/update")
                                .hasAnyRole("OPERATOR", "ADMIN")
                                .requestMatchers("/user/privacy/delete")
                                .hasRole("ADMIN")
                                .anyRequest()
                                    .authenticated());

        http.addFilterAt(
                        initialAuthenticationFilter,
                        BasicAuthenticationFilter.class)
                .addFilterAfter(
                        jwtAuthenticationFilter,
                        BasicAuthenticationFilter.class
                );
        return http.build();
    }
}
