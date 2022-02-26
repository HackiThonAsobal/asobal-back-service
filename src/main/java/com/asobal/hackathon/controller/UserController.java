package com.asobal.hackathon.controller;

import com.asobal.hackathon.domain.request.UserRequest;
import com.asobal.hackathon.domain.response.UserResponse;
import com.asobal.hackathon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/api/profile")
    public UserResponse getProfile(){
        String email = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().findFirst().get().toString();
        return userService.getUserProfile(email);
    }

    @PutMapping("/api/profile")
    public UserResponse setUpProfile(@Valid @RequestBody UserRequest userRequest){
        String email = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().findFirst().get().toString();
        return userService.setUserProfile(email, userRequest);
    }

}
