package com.ourhome.taehyeong.service;

import com.ourhome.taehyeong.authentication.dto.AuthUserDto;
import com.ourhome.taehyeong.entities.Otp;
import com.ourhome.taehyeong.entities.Privacy;
import com.ourhome.taehyeong.entities.User;
import com.ourhome.taehyeong.entities.dto.PayloadDto;
import com.ourhome.taehyeong.entities.enums.Role;
import com.ourhome.taehyeong.repository.OtpRepository;
import com.ourhome.taehyeong.repository.PrivacyRepository;
import com.ourhome.taehyeong.repository.UserRepository;
import com.ourhome.taehyeong.utils.GenerateCodeUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Value("${jwt.signing.key}")
    private String signingKey;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PrivacyRepository privacyRepository;

    public void addUser(User user) {
        if (!checkDuplicate(user.getUsername()))
            throw new IllegalArgumentException("username already exist");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_EMPLOYEE);
        Privacy privacy = new Privacy();
        privacy.setUniversity("not registered");
        privacy.setName("not registered");
        privacy.setBirth(new Date(0));
        privacy.setAddress("not registered");
        privacyRepository.save(privacy);
        user.setPrivacy(privacy);
        userRepository.save(user);
    }

    private boolean checkDuplicate(String username) {
        Optional<User> found = userRepository.findUserByUsername(username);
        return found.isEmpty();
    }



    public void auth(AuthUserDto user) {
        Optional<User> o =
                userRepository.findUserByUsername(user.getUsername());

        if(o.isPresent()) {
            User u = o.get();
            if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                renewOtp(u);
            } else {
                throw new BadCredentialsException("Bad credentials.");
            }
        } else {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    public boolean checkOtp(String username, String code) {
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(username);
        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            return code.equals(otp.getCode());
        }

        return false;
    }

    private void renewOtp(User u) {
        String code = GenerateCodeUtil.generateCode();

        Optional<Otp> userOtp = otpRepository.findOtpByUsername(u.getUsername());
        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            otp.setCode(code);
        } else {
            Otp otp = new Otp();
            otp.setUsername(u.getUsername());
            otp.setCode(code);
            otpRepository.save(otp);
        }
    }

    public Role getRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username).orElse(Role.ROLE_EMPLOYEE);
    }

    public PayloadDto findUsernameByToken(String jwt) {

        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        String username = String.valueOf(claims.get("username"));
        String role = String.valueOf(claims.get("role"));

        return PayloadDto.builder()
                .username(username)
                .role(role).build();
    }

}
