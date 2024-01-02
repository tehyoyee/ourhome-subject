package com.ourhome.taehyeong.service;

import com.ourhome.taehyeong.authentication.model.AuthUserDto;
import com.ourhome.taehyeong.entities.Otp;
import com.ourhome.taehyeong.entities.User;
import com.ourhome.taehyeong.entities.enums.Role;
import com.ourhome.taehyeong.repository.OtpRepository;
import com.ourhome.taehyeong.repository.UserRepository;
import com.ourhome.taehyeong.utils.GenerateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_GUEST);
        userRepository.save(user);
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
        return userRepository.findRoleByUsername(username).orElse(Role.ROLE_GUEST);
    }

}
