package com.asobal.hackathon.service;

import com.asobal.hackathon.domain.request.FeedScoreRequest;

public interface FeedScoreService {
    void setUpFeedScore(String email, FeedScoreRequest feedScoreRequest);
}
