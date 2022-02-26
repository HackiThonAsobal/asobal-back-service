package com.asobal.hackathon.service;

import com.asobal.hackathon.domain.request.FeedContentRequest;
import com.asobal.hackathon.domain.response.FeedResponse;

import java.util.List;

public interface FeedService {
    List<FeedResponse> getFeedList(String email);

    void setUpFeedContent(String email, FeedContentRequest feedContentRequest);

    void likeAction(String email, String postId);
}
