package com.ourhome.taehyeong.controller;

import com.ourhome.taehyeong.authentication.model.AuthUserDto;
import com.ourhome.taehyeong.entities.Otp;
import com.ourhome.taehyeong.entities.User;
import com.ourhome.taehyeong.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/add")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

//    @PutMapping("/user/modify-role")
//    public void addUser(@RequestBody String username) {
//        userService.modifyRole(username);
//    }
//    @PostMapping("/user/auth")
//    public void auth(@RequestBody AuthUserDto authUserDto) {
//        userService.auth(authUserDto);
//    }
//
//    @PostMapping("/otp/check")
//    public void check(@RequestBody Otp otp, HttpServletResponse response) {
//        if (userService.checkOtp(otp)) {
//            response.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        }
//    }
}