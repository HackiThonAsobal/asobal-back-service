package com.asobal.hackathon.service;

import com.asobal.hackathon.domain.request.UserRequest;
import com.asobal.hackathon.domain.response.UserResponse;

public interface UserService {
    UserResponse getUserProfile(String mail);

    UserResponse setUserProfile(String email, UserRequest userRequest);
}
