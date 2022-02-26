package com.asobal.hackathon.service.impl;

import com.asobal.hackathon.domain.request.FeedScoreRequest;
import com.asobal.hackathon.model.FeedScore;
import com.asobal.hackathon.model.User;
import com.asobal.hackathon.repository.FeedScoreRepository;
import com.asobal.hackathon.repository.UserRepository;
import com.asobal.hackathon.service.FeedScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedScoreServiceImpl implements FeedScoreService {

    @Autowired
    FeedScoreRepository feedScoreRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void setUpFeedScore(String email, FeedScoreRequest feedScoreRequest) {
        User userInfo = userRepository.findAllByEmail(email).get(0);
        feedScoreRepository.save(FeedScore.builder()
                .objectId(feedScoreRequest.getId())
                .type(feedScoreRequest.getType())
                .userId(userInfo.getId())
                .timeSpent(feedScoreRequest.getTime())
                .build()
        );
    }
}
