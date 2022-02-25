package com.asobal.hackathon.service;

import com.asobal.hackathon.domain.response.UserProfile;
import org.springframework.stereotype.Service;

public interface UserService {
    UserProfile getUserProfile(String mail);
}
