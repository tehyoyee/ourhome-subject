package com.ourhome.taehyeong.authentication.provider;

import com.ourhome.taehyeong.authentication.OtpAuthentication;
//import com.ourhome.taehyeong.authentication.proxy.AuthenticationServerProxy;
import com.ourhome.taehyeong.authentication.model.AuthUserDto;
import com.ourhome.taehyeong.entities.Otp;
import com.ourhome.taehyeong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String code = String.valueOf(authentication.getCredentials());

        boolean result = userService.checkOtp(username, code);

        if (result) {
            return new OtpAuthentication(username, code);
        } else {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return OtpAuthentication.class.isAssignableFrom(aClass);
    }
}