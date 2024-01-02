package com.ourhome.taehyeong.authentication.provider;

import com.ourhome.taehyeong.authentication.UsernamePasswordAuthentication;
import com.ourhome.taehyeong.authentication.model.AuthUserDto;
//import com.ourhome.taehyeong.authentication.proxy.AuthenticationServerProxy;
//import com.ourhome.taehyeong.entities.User;
import com.ourhome.taehyeong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        AuthUserDto authUserDto = AuthUserDto.builder()
                .username(username)
                .password(password).build();
        userService.auth(authUserDto);
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(aClass);
    }
}