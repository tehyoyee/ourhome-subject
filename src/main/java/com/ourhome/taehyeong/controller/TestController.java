package com.ourhome.taehyeong.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Test";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/guest")
    public String guest() {
        return "guest";
    }

    @GetMapping("/employee")
    public String employee() {
        return "employee";
    }
}