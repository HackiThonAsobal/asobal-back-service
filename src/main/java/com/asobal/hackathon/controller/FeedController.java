package com.asobal.hackathon.controller;

import com.asobal.hackathon.domain.request.FeedContentRequest;
import com.asobal.hackathon.domain.response.FeedResponse;
import com.asobal.hackathon.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedController {

    @Autowired
    FeedService feedService;

    @GetMapping("/api/feed")
    public List<FeedResponse> getFeed() {
        String email = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().findFirst().get().toString();
        return feedService.getFeedList(email);
    }

    @PostMapping("api/feed")
    public List<FeedResponse> setUpFeedContent(@RequestBody FeedContentRequest feedContentRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().findFirst().get().toString();

        feedService.setUpFeedContent(email, feedContentRequest);
        return feedService.getFeedList(email);
    }
}
