package com.asobal.hackathon.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("like")
@Data
@Builder
public class Like {
    @Id
    private String id;
    private String postId;
    private String userId;
}
