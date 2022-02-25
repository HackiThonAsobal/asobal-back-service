package com.asobal.hackathon.service.impl;

import com.asobal.hackathon.domain.response.UserProfile;
import com.asobal.hackathon.model.User;
import com.asobal.hackathon.repository.UserRepository;
import com.asobal.hackathon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserProfile getUserProfile(String email) {
        User target;
        List<User> userList = userRepository.findAllByEmail(email);

        if (userList.isEmpty()) {
            target = userRepository.save(User.builder().email(email).build());
        } else {
            target = userList.get(0);
        }

        return UserProfile.builder()
                .name(target.getName())
                .lastName(target.getLastName())
                .gender(target.getGender())
                .teamId(target.getTeamId())
                .birthDate(target.getBirthDate())
                .type(target.getType())
                .build();
    }
}
