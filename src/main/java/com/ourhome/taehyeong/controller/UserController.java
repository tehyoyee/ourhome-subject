package com.ourhome.taehyeong.controller;

import com.ourhome.taehyeong.entities.Privacy;
import com.ourhome.taehyeong.entities.User;
import com.ourhome.taehyeong.entities.dto.PrivacyDto;
import com.ourhome.taehyeong.service.AuthService;
import com.ourhome.taehyeong.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public void addUser(@RequestBody User user) { authService.addUser(user);
    }

    @DeleteMapping("/delete")
    public void deleteUserByUsername(@RequestBody String name) {
        userService.deleteUserByUsername(name);
    }

    @PutMapping("/privacy/update")
    public void updatePrivacy(HttpServletRequest request, @RequestBody PrivacyDto updatePrivacyDto) {
        userService.updatePrivacy(request, updatePrivacyDto);
    }

    @GetMapping("/privacy")
    public List<Privacy> getPrivacy(@RequestParam String columnName,
                                    @RequestParam String arg) {

        return userService.getPrivacyByParam(columnName, arg);
    }

    @GetMapping("/privacy/all")
    public List<Privacy> getPrivacyAll() {

        return userService.getPrivacyAll();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        // return ResponseEntity.badRequest().body(e.getMessage()); // 위와 같은 코드
    }

}