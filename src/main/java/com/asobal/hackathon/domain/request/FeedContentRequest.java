package com.asobal.hackathon.domain.request;

import lombok.Data;

@Data
public class FeedContentRequest {
    private String tittle;
    private String description;
    private String image;
    private String type;
    // POST or COMMENT
    private String linkedPostId;
}
