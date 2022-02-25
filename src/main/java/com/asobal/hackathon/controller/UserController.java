package com.asobal.hackathon.controller;

import com.asobal.hackathon.domain.response.UserProfile;
import com.asobal.hackathon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/api/profile")
    public UserProfile getProfile(){
        String email = "test@test.com";
        return userService.getUserProfile(email);
    }
}
