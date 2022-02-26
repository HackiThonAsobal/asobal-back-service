package com.asobal.hackathon.domain.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {
    private String description;
    private String userName;
    private String userId;
}
