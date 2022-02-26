package com.asobal.hackathon.service;

import com.asobal.hackathon.domain.response.FeedResponse;

import java.util.List;

public interface FeedService {
    List<FeedResponse> getFeedList(String email);
}
