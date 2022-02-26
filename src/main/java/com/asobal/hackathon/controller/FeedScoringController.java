package com.asobal.hackathon.controller;

import com.asobal.hackathon.domain.request.FeedScoreRequest;
import com.asobal.hackathon.service.FeedScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class FeedScoringController {

    @Autowired
    FeedScoreService feedScoreService;

    @PostMapping("/api/feed/score")
    public void setUpFeedScore(@Valid @RequestBody FeedScoreRequest feedScoreRequest){
        String email = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().findFirst().get().toString();

        feedScoreService.setUpFeedScore(email, feedScoreRequest);
    }
}
