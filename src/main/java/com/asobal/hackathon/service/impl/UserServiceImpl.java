package com.asobal.hackathon.service.impl;

import com.asobal.hackathon.domain.request.UserRequest;
import com.asobal.hackathon.domain.response.UserResponse;
import com.asobal.hackathon.model.User;
import com.asobal.hackathon.repository.UserRepository;
import com.asobal.hackathon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserResponse getUserProfile(String email) {
        User target;
        List<User> userList = userRepository.findAllByEmail(email);

        if (userList.isEmpty()) {
            target = userRepository.save(User.builder().email(email).build());
        } else {
            target = userList.get(0);
        }

        return UserResponse.builder()
                .name(target.getName())
                .lastName(target.getLastName())
                .gender(target.getGender())
                .teamId(target.getTeamId())
                .birthDate(target.getBirthDate())
                .type(target.getType())
                .build();
    }

    @Override
    public UserResponse setUserProfile(String email, UserRequest userRequest) {
        List<User> userList = userRepository.findAllByEmail(email);
        userList.get(0).setEmail(email);
        userList.get(0).setName(userRequest.getName());
        userList.get(0).setLastName(userRequest.getLastName());
        userList.get(0).setGender(userRequest.getGender());
        userList.get(0).setTeamId(userRequest.getTeamId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        userList.get(0).setBirthDate(LocalDate.parse(userRequest.getBirthDate(), formatter));
        userList.get(0).setType("USER");
        userList.get(0).setEmail(email);

        User x = userRepository.save(userList.get(0));
        return UserResponse.builder()
                .name(x.getName())
                .lastName(x.getLastName())
                .gender(x.getGender())
                .teamId(x.getTeamId())
                .birthDate(x.getBirthDate())
                .type(x.getType())
                .build();
    }
}
