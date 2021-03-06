package com.asobal.hackathon.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponse {
    private String id;
    private String title;
    private String description;
    private String image;
    private String type;
    private String teamOneImage;
    private String teamTwoImage;
    private long likes;
    private Boolean isLiked;
    private List<CommentResponse> comments;
    private LocalDateTime publicationDate;
    private LocalDateTime matchDate;
    private String userId;
    private List<String> tags;
}
