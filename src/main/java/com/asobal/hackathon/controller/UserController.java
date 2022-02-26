package com.asobal.hackathon.controller;

import com.asobal.hackathon.domain.request.UserRequest;
import com.asobal.hackathon.domain.response.UserResponse;
import com.asobal.hackathon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/api/profile")
    public UserResponse getProfile(){
        String email = "test@test.com";
        return userService.getUserProfile(email);
    }

    @PutMapping("/api/profile")
    public UserResponse setUpProfile(@RequestBody UserRequest userRequest){
        String email = "test@test.com";
        return userService.setUserProfile(email, userRequest);
    }

}
